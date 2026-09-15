package pages.booking;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class PaymentSummaryStep {

    private final Page page;
    private final Locator title;
    private final Locator passengerItems;
    private final Locator totalAmount;
    private final Locator backButton;
    private final Locator payButton;
    private final Locator error;


    public PaymentSummaryStep(Page page) {
        this.page = page;
        this.title = page.getByTestId("booking-summary-title");
        this.passengerItems = page.getByTestId("booking-summary-passenger-item");
        this.totalAmount = page.getByTestId("booking-summary-total-amount");
        this.backButton = page.getByTestId("booking-summary-back-button");
        this.payButton = page.getByTestId("booking-summary-continue-button");
        this.error = page.getByTestId("booking-summary-error");
    }

    public ConfirmationStep clickPay()
    {
        this.payButton.click();
        return new ConfirmationStep(page);
    }

    public PassengersStep clickBack()
    {
        this.backButton.click();
        return new PassengersStep(page);
    }

    public Locator getTitle() {
        return title;
    }

    public Locator getPassengerItems() {
        return passengerItems;
    }

    public Locator getTotalAmount() {
        return totalAmount;
    }

    public Locator getError() {
        return error;
    }
}
