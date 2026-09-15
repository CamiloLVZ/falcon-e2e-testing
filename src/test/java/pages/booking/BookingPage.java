package pages.booking;

import com.microsoft.playwright.Page;

public class BookingPage {

    private final Page page;
    private final ConfirmFlightStep confirmFlightStep;
    private final PassengersStep passengersStep;
    private final PaymentSummaryStep paymentSummaryStep;
    private final ConfirmationStep confirmationStep;

    public BookingPage(Page page) {
        this.page = page;
        this.confirmFlightStep = new ConfirmFlightStep(page);
        this.passengersStep = new PassengersStep(page);
        this.paymentSummaryStep = new PaymentSummaryStep(page);
        this.confirmationStep = new ConfirmationStep(page);
    }

    public ConfirmFlightStep confirmFlightStep() {
        return confirmFlightStep;
    }
    public PassengersStep passengersStep() {
        return passengersStep;
    }
    public PaymentSummaryStep paymentSummaryStep() {
        return paymentSummaryStep;
    }
    public ConfirmationStep confirmationStep() {
        return confirmationStep;
    }

}
