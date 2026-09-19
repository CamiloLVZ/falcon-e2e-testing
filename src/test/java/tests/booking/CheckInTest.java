package tests.booking;

import base.BaseTest;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import config.TestConfig;
import fixtures.BookingFlowHelper;
import fixtures.Reservation;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.CheckInPage;

import java.time.LocalDate;

@Tag("debug")
@Tag("regression")
public class CheckInTest extends BaseTest {

    @Test
    public void checkInPositive(){
        BookingFlowHelper bookingFlowHelper = new BookingFlowHelper(page);
        LocalDate date = LocalDate.now().plusDays(1);
        Reservation reservation = bookingFlowHelper.createStandardReservation("BOG","CTG", date);

        navigateSafely(TestConfig.baseUrl()+"/check-in");
        CheckInPage checkInPage = new CheckInPage(page);

        checkInPage.checkIn(reservation.reservationNumber(), reservation.contactEmail(), reservation.identificationNumber(), reservation.countryIso());

        PlaywrightAssertions.assertThat(checkInPage.getError()).isHidden();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessTitle()).isVisible();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessDownloadButton()).isVisible();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessPassengerName()).isVisible();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessSeatClass()).isVisible();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessNewCheckInButton()).isVisible();
    }

    @Test
    public void checkInNegative(){
        navigateSafely(TestConfig.baseUrl()+"/check-in");
        CheckInPage checkInPage = new CheckInPage(page);

        checkInPage.checkInFailed();

        PlaywrightAssertions.assertThat(checkInPage.getError()).isVisible();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessTitle()).isHidden();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessDownloadButton()).isHidden();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessPassengerName()).isHidden();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessSeatClass()).isHidden();
        PlaywrightAssertions.assertThat(checkInPage.getSuccessNewCheckInButton()).isHidden();
    }

}
