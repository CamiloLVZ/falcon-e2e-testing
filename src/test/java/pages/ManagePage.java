package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ManagePage {
    private final Page page;
    private final Locator title;
    private final Locator reservationNumberInput;
    private final Locator contactEmailInput;
    private final Locator error;
    private final Locator searchButton;

    private final Locator reservationNumberLabel;
    private final Locator passengerItems;
    private final Locator flightRoute;
    private final Locator newSearchButton;
    public ManagePage(Page page) {
        this.page = page;
        title = page.getByTestId("manage-title");
        reservationNumberInput = page.getByTestId("manage-reservation-number-input");
        contactEmailInput = page.getByTestId("manage-contact-email-input");
        error = page.getByTestId("manage-error");
        searchButton = page.getByTestId("manage-search-button");

        this.reservationNumberLabel = page.getByTestId("manage-details-reservation-number-label");
        this.passengerItems = page.getByTestId("manage-details-passenger-item");
        this.flightRoute = page.getByTestId("manage-details-flight-number-label");
        this.newSearchButton = page.getByTestId("manage-details-new-search-button");
    }

    public void searchReservation(String reservationNumber, String contactEmail){
        reservationNumberInput.clear();
        reservationNumberInput.fill(reservationNumber);
        contactEmailInput.clear();
        contactEmailInput.fill(contactEmail);
        searchButton.click();
    }

    public Locator getTitle(){
        return title;
    }

    public Locator getReservationNumberLabel() {
        return reservationNumberLabel;
    }

    public Locator getPassengerItems() {
        return passengerItems;
    }

    public Locator getError() {
        return error;
    }

}
