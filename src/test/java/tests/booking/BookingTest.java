package tests.booking;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.FlightResultsPage;
import pages.HomePage;
import pages.booking.*;
import pages.components.PassengerFormFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Tag("regression")
public class BookingTest extends BaseTest {

    private BookingPage navigateToBookingPage() {
        LocalDate date = LocalDate.now().plusDays(5);
        FlightResultsPage results = new HomePage(page)
                .searchFlights(
                        "BOG",
                        "CLO",
                        date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        return results.clickBookFlight(0);
    }

    private ConfirmFlightStep navigateToConfirmFlightStep() {
        return navigateToBookingPage().confirmFlightStep();
    }

    private PassengersStep navigateToPassengersStep() {
        ConfirmFlightStep confirmFlightStep = navigateToConfirmFlightStep();
        confirmFlightStep.selectFirstClass();
        confirmFlightStep.enterEmail("e2e.client@falcon.test");
        return confirmFlightStep.clickContinue();
    }

    private PaymentSummaryStep navigateToPaymentSummaryStep(String firstName, String lastName, String dateOfBirth, String identificationNumber, String nationality) {
        PassengersStep passengersStep = navigateToPassengersStep();
        PassengerFormFragment form = passengersStep.getPassengerForm(0);
        form.fill(firstName, lastName, dateOfBirth, identificationNumber, nationality);
        return passengersStep.clickContinue();
    }

    private ConfirmationStep navigateToConfirmationStep(String firstName, String lastName, String dateOfBirth, String identificationNumber, String nationality) {
        PaymentSummaryStep paymentSummaryStep = navigateToPaymentSummaryStep(firstName, lastName, dateOfBirth, identificationNumber, nationality);

        return paymentSummaryStep.clickPay();
    }

    @Test
    public void bookButton() {
        ConfirmFlightStep confirmFlightStep = navigateToConfirmFlightStep();
        Locator bookingTitle = confirmFlightStep.getTitle();
        PlaywrightAssertions.assertThat(bookingTitle).isVisible();
    }

    @Test
    public void continueWithoutEmail() {
        ConfirmFlightStep confirmFlightStep = navigateToConfirmFlightStep();
        confirmFlightStep.clickContinue();

        PlaywrightAssertions.assertThat(confirmFlightStep.getError()).isVisible();
    }

    @Test
    public void addPassengers() {
        PassengersStep passengersStep = navigateToPassengersStep();

        passengersStep.clickAddPassenger();

        PlaywrightAssertions.assertThat(passengersStep.getPassengerForms()).hasCount(2);

    }

    @Test
    public void mandatoryFields() {
        PassengersStep passengersStep = navigateToPassengersStep();

        PaymentSummaryStep paymentSummaryStep = passengersStep.clickContinue();

        PlaywrightAssertions.assertThat(passengersStep.getError()).isVisible();
        PlaywrightAssertions.assertThat(paymentSummaryStep.getTitle()).isHidden();

    }

    @Test
    public void backButton() {
        String firstName = "John";
        String lastName = "Doe";
        String dateOfBirth = "1999-12-31";
        String identificationNumber = generateRandomIdentification();
        String nationality = "Colombia";

        PaymentSummaryStep paymentSummaryStep = navigateToPaymentSummaryStep(firstName, lastName, dateOfBirth, identificationNumber, nationality);
        PassengersStep passengersStep = paymentSummaryStep.clickBack();

        PassengerFormFragment form = passengersStep.getPassengerForm(0);

        PlaywrightAssertions.assertThat(form.getFirstNameInput()).hasValue(firstName);
        PlaywrightAssertions.assertThat(form.getLastNameInput()).hasValue(lastName);
        PlaywrightAssertions.assertThat(form.getDateOfBirthInput()).hasValue(dateOfBirth);
        PlaywrightAssertions.assertThat(form.getIdentificationInput()).hasValue(identificationNumber);
        PlaywrightAssertions.assertThat(form.getNationalitySelect()).hasValue("CO");
    }

    @Test
    public void checkPaymentSummary() {
        ConfirmFlightStep confirmFlightStep = navigateToConfirmFlightStep();
        Integer firstClassUnitPrice = stringPriceToInteger(confirmFlightStep.getFirstClassUnitPrice());

        confirmFlightStep.selectFirstClass();
        confirmFlightStep.enterEmail("e2e.client@falcon.test");
        PassengersStep passengersStep = confirmFlightStep.clickContinue();

        PassengerFormFragment form = passengersStep.getPassengerForm(0);
        form.fill("Luck", "Doe", "1999-12-31", generateRandomIdentification(), "Colombia");
        PaymentSummaryStep paymentSummaryStep = passengersStep.clickContinue();

        PlaywrightAssertions.assertThat(paymentSummaryStep.getPassengerItems()).hasCount(1);
        PlaywrightAssertions.assertThat(paymentSummaryStep.getPassengerItems().first().getByTestId("passenger-name")).containsText("Luck Doe");
        PlaywrightAssertions.assertThat(paymentSummaryStep.getPassengerItems().first().getByTestId("passenger-class")).containsText("Primera clase");
        PlaywrightAssertions.assertThat(paymentSummaryStep.getTotalAmount()).containsText(intPriceToString(firstClassUnitPrice));
    }

    @Test
    public void checkConfirmation() {
        ConfirmationStep confirmationStep = navigateToConfirmationStep("Mack",
                "Doe",
                "1999-12-31",
                generateRandomIdentification(),
                "Colombia");

        PlaywrightAssertions.assertThat(confirmationStep.getTitle()).isVisible();
        PlaywrightAssertions.assertThat(confirmationStep.getReservationNumber()).isVisible();
        PlaywrightAssertions.assertThat(confirmationStep.getTotalAmount()).isVisible();
    }

    @Test
    public void backHomeButton() {
        ConfirmationStep confirmationStep = navigateToConfirmationStep("Martin",
                "Doe",
                "1999-12-31",
                generateRandomIdentification(),
                "Colombia");
        HomePage homePage = confirmationStep.clickGoHome();

        PlaywrightAssertions.assertThat(homePage.getSearchButton()).isVisible();
    }

}
