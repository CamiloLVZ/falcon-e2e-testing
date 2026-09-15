package pages.booking;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.HomePage;

public class ConfirmationStep {

    private final Page page;
    private final Locator title;
    private final Locator reservationNumber;
    private final Locator totalAmount;
    private final Locator homeButton;

    public ConfirmationStep(Page page) {
        this.page = page;
        this.title = page.getByTestId("booking-confirmation-title");
        this.reservationNumber = page.getByTestId("booking-confirmation-reservation-number");
        this.totalAmount = page.getByTestId("booking-confirmation-total-amount");
        this.homeButton = page.getByTestId("booking-confirmation-home-button");
    }

    public HomePage clickGoHome(){
        this.homeButton.click();
        return new HomePage(page);
    }

    public Locator getTitle() {
        return title;
    }
    public Locator getReservationNumber() {
        return reservationNumber;
    }
    public Locator getTotalAmount() {
        return totalAmount;
    }
}
