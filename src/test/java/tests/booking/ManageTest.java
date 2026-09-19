package tests.booking;

import base.BaseTest;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import config.TestConfig;
import fixtures.BookingFlowHelper;
import fixtures.Reservation;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ManagePage;

import java.time.LocalDate;

@Tag("regression")
public class ManageTest extends BaseTest {

    @Test
    public void searchReservationPositive(){
        BookingFlowHelper bookingFlowHelper = new BookingFlowHelper(page);
        LocalDate date = LocalDate.now().plusDays(5);
        Reservation reservation = bookingFlowHelper.createStandardReservation("BOG","ADZ", date);

        navigateSafely(TestConfig.baseUrl()+"/manage");
        ManagePage managePage = new ManagePage(page);
        managePage.searchReservation(reservation.reservationNumber(), reservation.contactEmail());

        PlaywrightAssertions.assertThat(managePage.getError()).isHidden();
        PlaywrightAssertions.assertThat(managePage.getReservationNumberLabel()).containsText(reservation.reservationNumber());
    }

    @Test
    public void searchReservationNegative(){
        BookingFlowHelper bookingFlowHelper = new BookingFlowHelper(page);
        LocalDate date = LocalDate.now().plusDays(5);
        Reservation reservation = bookingFlowHelper.createStandardReservation("BOG","CLO", date);

        navigateSafely(TestConfig.baseUrl()+"/manage");
        ManagePage managePage = new ManagePage(page);
        managePage.searchReservation(reservation.reservationNumber(), "incorrect@email.com");

        PlaywrightAssertions.assertThat(managePage.getError()).isVisible();
        PlaywrightAssertions.assertThat(managePage.getReservationNumberLabel()).isHidden();
        PlaywrightAssertions.assertThat(managePage.getPassengerItems()).isHidden();
    }


}
