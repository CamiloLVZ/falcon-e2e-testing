package tests.concurrency;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import config.TestConfig;
import fixtures.BookingRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;

@Tag("concurrency")
public class BookingConcurrencyTest extends BaseTest {

    private static final String FLIGHT_NUMBER = "CONC01";

    private String findConcurrencyFlightId() {
        try (Playwright p = Playwright.create()) {
            APIRequestContext request = p.request().newContext();
            APIResponse response = request.get(
                    TestConfig.apiBaseUrl() + "/v1/flights"
                            + "?flightNumber=" + FLIGHT_NUMBER
                            + "&page=0"
                            + "&size=1",
                    RequestOptions.create().setHeader("Content-Type", "application/json")
            );

            JsonNode body = new ObjectMapper().readTree(response.text());
            JsonNode content = body.get("content");

            if (content == null || content.isEmpty()) {
                throw new IllegalStateException(
                        "Concurrency test flight " + FLIGHT_NUMBER + " not found. " +
                                "Make sure the seed was applied and docker compose is up."
                );
            }

            return content.get(0).get("id").asText();

        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching concurrency test flight", e);
        }
    }

    private BookingRequest buildBookingRequest(String flightId) {
        BookingRequest payload = new BookingRequest();
        payload.setFlightId(flightId);
        payload.setContactEmail(CLIENT_USER_EMAIL);

        BookingRequest.PassengerDetails passenger = new BookingRequest.PassengerDetails();
        passenger.setFirstName("Concurrent");
        passenger.setLastName("Tester");
        passenger.setNationalityIsoCode("CO");
        passenger.setDateOfBirth("1995-07-16");
        passenger.setIdentificationNumber(generateRandomIdentification());

        BookingRequest.PassengerItem item = new BookingRequest.PassengerItem();
        item.setPassenger(passenger);
        item.setSeatClass("FIRST_CLASS");
        item.setUnitPrice(0);

        payload.setPassengers(List.of(item));
        return payload;
    }

    private ReservationResult postReservation(String flightId) {
        try (Playwright threadPlaywright = Playwright.create()) {
            APIRequestContext request = threadPlaywright.request().newContext();
            APIResponse response = request.post(
                    TestConfig.apiBaseUrl() + "/v1/payments",
                    RequestOptions.create()
                            .setHeader("Content-Type", "application/json")
                            .setData(buildBookingRequest(flightId))
            );
            return new ReservationResult(response.status(), response.text());
        }
    }

    @Test
    public void twoSimultaneousBookingsForLastSeat() throws InterruptedException, ExecutionException {
        String flightId = findConcurrencyFlightId();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        Future<ReservationResult> futureA = executor.submit(() -> {
            latch.await();
            return postReservation(flightId);
        });

        Future<ReservationResult> futureB = executor.submit(() -> {
            latch.await();
            return postReservation(flightId);
        });

        latch.countDown();
        executor.shutdown();

        ReservationResult resultA = futureA.get();
        ReservationResult resultB = futureB.get();

        List<Integer> statuses = List.of(resultA.status(), resultB.status());

        long successCount = statuses.stream().filter(s -> s == 201).count();
        Assertions.assertEquals(1, successCount,
                "Exactly one booking should succeed. Statuses were: " + statuses);

        long failureCount = statuses.stream().filter(s -> s == 400).count();
        Assertions.assertEquals(1, failureCount,
                "Exactly one booking should fail with 400. Statuses were: " + statuses);
    }

    private record ReservationResult(int status, String body) {}
}