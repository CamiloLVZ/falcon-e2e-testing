package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.components.SeatSelectionComponent;

public class CheckInPage {
    private final Page page;
    private final Locator title;
    private final Locator reservationNumberInput;
    private final Locator contactEmailInput;
    private final Locator identificationNumberInput;
    private final Locator countrySelect;
    private final Locator error;
    private final Locator continueButton;

    private final SeatSelectionComponent seatSelectionComponent;

    private final Locator successTitle;
    private final Locator successPassengerName;
    private final Locator successSeatClass;
    private final Locator successDownloadButton;
    private final Locator successNewCheckInButton;



    public CheckInPage(Page page) {
        this.page = page;
        title = page.getByTestId("checkin-title");
        reservationNumberInput = page.getByTestId("checkin-reservation-number-input");
        contactEmailInput = page.getByTestId("checkin-contact-email-input");
        identificationNumberInput = page.getByTestId("checkin-identification-number-input");
        countrySelect = page.getByTestId("checkin-country-select");
        error = page.getByTestId("checkin-error");
        continueButton = page.getByTestId("checkin-continue-button");

        seatSelectionComponent = new SeatSelectionComponent(page.getByTestId("airplane-seat-map"));

        successTitle = page.getByTestId("checkin-success-title");
        successPassengerName = page.getByTestId("checkin-success-passenger-name");
        successSeatClass = page.getByTestId("checkin-success-seat-class");
        successDownloadButton = page.getByTestId("checkin-success-download-button");
        successNewCheckInButton = page.getByTestId("checkin-success-new-checkin-button");

    }

    public Locator getTitle(){
        return title;

    }
    public void clickContinue(){
        continueButton.click();
    }

    public Locator getError(){
        return error;
    }

    public Locator getSuccessTitle() {
        return successTitle;
    }

    public Locator getSuccessPassengerName() {
        return successPassengerName;
    }

    public Locator getSuccessSeatClass() {
        return successSeatClass;
    }

    public Locator getSuccessDownloadButton() {
        return successDownloadButton;
    }

    public Locator getSuccessNewCheckInButton() {
        return successNewCheckInButton;
    }

    public void checkIn(String reservationNumber, String contactEmail, String identificationNumber, String countryIsoCode){
        reservationNumberInput.clear();
        reservationNumberInput.fill(reservationNumber);
        contactEmailInput.clear();
        contactEmailInput.fill(contactEmail);
        identificationNumberInput.clear();
        identificationNumberInput.fill(identificationNumber);

        countrySelect.selectOption(countryIsoCode);

        continueButton.click();

        seatSelectionComponent.selectFirstAvailableSeat();
    }

    public void checkInFailed(){
        reservationNumberInput.clear();
        reservationNumberInput.fill("ABC1234");
        contactEmailInput.clear();
        contactEmailInput.fill("invalid@email.test");
        identificationNumberInput.clear();
        identificationNumberInput.fill("12345");
        countrySelect.selectOption("CO");
        continueButton.click();
    }

}
