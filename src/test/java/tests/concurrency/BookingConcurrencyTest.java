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
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

public class BookingConcurrencyTest extends BaseTest {

    private String searchFlightId() {
        APIRequestContext request = playwright.request().newContext();
        String date = LocalDate.now().plusDays(10).toString();

        APIResponse response = request.get(TestConfig.apiBaseUrl() + "/v1/flights/search?origin=BOG&destination=MIA&date=" + date + "&status=SCHEDULED",
                RequestOptions.create().setHeader("Content-Type", "application/json"));

        String flightId = "";
        try {
            JsonNode responseBody = new ObjectMapper().readTree(response.text());

            if (responseBody.isArray()) {
                for (JsonNode node : responseBody) {
                    flightId = node.get("id").asText();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing flight response", e);
        }
        return flightId;
    }

    private int getFirstClassCapacity(String flightId) {
        APIRequestContext request = playwright.request().newContext();

        APIResponse response = request.get(TestConfig.apiBaseUrl() + "/v1/flights/" + flightId + "/seats",
                RequestOptions.create().setHeader("Content-Type", "application/json"));

        int firstClassRows = 0;
        String seatColumns = "";
        try {
            JsonNode responseBody = new ObjectMapper().readTree(response.text());

            seatColumns = responseBody.get("seatColumns").asText();
            firstClassRows = responseBody.get("firstClassRows").asInt();


        } catch (Exception e) {
            throw new RuntimeException("Error parsing flight seats response", e);
        }

        return firstClassRows * seatColumns.length();
    }

    private BookingRequest generateBody() {
        BookingRequest requestPayload = new BookingRequest();

        requestPayload.setContactEmail(CLIENT_USER_EMAIL);

        BookingRequest.PassengerDetails passengerDetails = new BookingRequest.PassengerDetails();
        passengerDetails.setFirstName("Juan");
        passengerDetails.setLastName("Perez");
        passengerDetails.setNationalityIsoCode("CO");
        passengerDetails.setDateOfBirth("1995-07-16");
        passengerDetails.setIdentificationNumber(generateRandomIdentification());

        BookingRequest.PassengerItem passengerItem = new BookingRequest.PassengerItem();
        passengerItem.setPassenger(passengerDetails);
        passengerItem.setSeatClass("FIRST_CLASS");
        passengerItem.setUnitPrice(0);

        requestPayload.setPassengers(List.of(passengerItem));
        return requestPayload;
    }

    public APIResponse postReservation(String flightId) {
        BookingRequest requestPayload = generateBody();
        requestPayload.setFlightId(flightId);

        try(Playwright threadPlaywright = Playwright.create()) {
            APIRequestContext request = threadPlaywright.request().newContext();

            APIResponse response = request.post(TestConfig.apiBaseUrl() + "/v1/payments",
                    RequestOptions.create().setHeader("Content-Type", "application/json")
                            .setData(requestPayload)
            );
            System.out.println(response.text());
            return response;
        }
    }


    public void fillFlight(String flightId) {
        int capacity = getFirstClassCapacity(flightId);
        for (int i = 1; i <= capacity-1; i++) {
            System.out.print(i);
            postReservation(flightId);
        }
    }

    @Test
    public void TwoBookingLastSeat() {
        String flightId = searchFlightId();
        fillFlight(flightId);

        System.out.println("-------------------CONCURRENT BOOKING REQUESTS--------------------------");

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        // Thread A
        Future<APIResponse> futureA = executor.submit(() -> {
            latch.await();
            return postReservation(flightId);
        });

        // Thread B
        Future<APIResponse> futureB = executor.submit(() -> {
            latch.await();
            return postReservation(flightId);
        });

        latch.countDown();

        APIResponse responseA;
        APIResponse responseB;
        try {
            responseA = futureA.get();
            responseB = futureB.get();
        }catch(ExecutionException | InterruptedException e){
            throw new RuntimeException(e);
        }
        List<Integer> statuses = List.of(responseA.status(), responseB.status());

        long successCount = statuses.stream().filter(s -> s == 201).count();
        Assertions.assertEquals(1, successCount);

        long failureCount = statuses.stream().filter(s -> s == 400).count();
        Assertions.assertEquals(1, failureCount);
    }


}
