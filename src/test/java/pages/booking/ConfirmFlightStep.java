package pages.booking;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ConfirmFlightStep {
    private final Page page;
    private final Locator title;
    private final Locator selectEconomyClassButton;
    private final Locator selectFirstClassButton;
    private final Locator emailInput;
    private final Locator continueButton;
    private final Locator error;


    public ConfirmFlightStep(Page page) {
        this.page = page;
        this.title = page.getByTestId("booking-confirm-flight-title");
        this.selectEconomyClassButton = page.getByTestId("booking-confirm-flight-economy-class-button");
        this.selectFirstClassButton = page.getByTestId("booking-confirm-flight-first-class-button");
        this.emailInput = page.getByTestId("booking-confirm-flight-email-input");
        this.continueButton = page.getByTestId("booking-confirm-flight-continue-button");
        this.error = page.getByTestId("booking-confirm-flight-error");
    }

    public void selectEconomyClass(){
        this.selectEconomyClassButton.click();
    }

    public void selectFirstClass(){
        this.selectFirstClassButton.click();
    }

    public void enterEmail(String email){
        this.emailInput.clear();
        this.emailInput.fill(email);
    }

    public PassengersStep clickContinue(){
        this.continueButton.click();
        return new PassengersStep(page);
    }

    public String getEconomyClassUnitPrice(){
        return selectEconomyClassButton.getByTestId("price").textContent();
    }

    public String getFirstClassUnitPrice(){
        return selectFirstClassButton.getByTestId("price").textContent();
    }

    public Locator getTitle() {
        return title;
    }

    public Locator getError() {
        return error;
    }
}
