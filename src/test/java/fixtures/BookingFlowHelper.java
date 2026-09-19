package fixtures;

import com.microsoft.playwright.Page;
import pages.HomePage;
import pages.booking.BookingPage;
import pages.booking.ConfirmationStep;
import pages.booking.PassengersStep;
import pages.booking.PaymentSummaryStep;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingFlowHelper {

    private final Page page;

    public BookingFlowHelper(Page page) {
        this.page = page;
    }

    public Reservation createStandardReservation(String origin, String destination, LocalDate date) {

        BookingPage bookingPage = new HomePage(page)
                .searchFlights(origin, destination, date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .clickBookFlight(0);

        bookingPage.confirmFlightStep().selectFirstClass();
        bookingPage.confirmFlightStep().enterEmail("e2e.client@falcon.test");
        PassengersStep passengersStep = bookingPage.confirmFlightStep().clickContinue();

        String identificationNumber = "10" + System.currentTimeMillis() % 10000000;
        passengersStep.getPassengerForm(0).fill("Carlos", "Gomez", "1990-05-15", identificationNumber, "Colombia");
        PaymentSummaryStep summaryStep = passengersStep.clickContinue();

        ConfirmationStep confirmationStep = summaryStep.clickPay();
        String reservationNumber = confirmationStep.getReservationNumber().innerText();

        return new Reservation(reservationNumber, "e2e.client@falcon.test", identificationNumber, "CO");
    }
}