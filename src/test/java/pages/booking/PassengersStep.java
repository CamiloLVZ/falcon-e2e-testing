package pages.booking;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.components.PassengerFormFragment;

public class PassengersStep {

    private final Page page;
    private final Locator title;
    private final Locator addPassengerButtons;
    private final Locator passengerForms;
    private final Locator backButton;
    private final Locator continueButton;
    private final Locator error;

    public PassengersStep(Page page) {
        this.page = page;
        this.title = page.getByTestId("booking-passengers-title");
        this.addPassengerButtons= page.getByTestId("booking-passengers-add-passenger-button");
        this.passengerForms= page.getByTestId("booking-passengers-passenger-form");
        this.backButton= page.getByTestId("booking-passengers-back-button");
        this.continueButton= page.getByTestId("booking-passengers-continue-button");
        this.error= page.getByTestId("booking-passengers-error");
    }

    public void clickAddPassenger(){
        this.addPassengerButtons.click();
    }

    public PaymentSummaryStep clickContinue(){
        this.continueButton.click();
        return new PaymentSummaryStep(page);
    }

    public ConfirmFlightStep clickBack()
    {
        this.backButton.click();
        return new ConfirmFlightStep(page);
    }

    public Locator getPassengerForms(){
        return this.passengerForms;
    }

    public Locator getError(){
        return this.error;
    }

    public PassengerFormFragment getPassengerForm(int index) {
        return new PassengerFormFragment(passengerForms.nth(index));
    }

}
