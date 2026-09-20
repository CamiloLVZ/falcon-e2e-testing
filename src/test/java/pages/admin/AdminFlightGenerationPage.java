package pages.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AdminFlightGenerationPage {

    private final Page page;

    private final Locator pageHeading;
    private final Locator generateFlightsButton;
    private final Locator generateFlightsDrawer;
    private final Locator flightGenerateForm;
    private final Locator routeInput;
    private final Locator submitButton;
    private final Locator errorMessage;

    public AdminFlightGenerationPage(Page page) {
        this.page = page;
        this.pageHeading = page.getByTestId("flight-generation-page-heading");
        this.generateFlightsButton = page.getByTestId("generate-flights-btn");
        this.generateFlightsDrawer = page.getByTestId("generate-flights-drawer");
        this.flightGenerateForm = page.getByTestId("flight-generate-form");
        this.routeInput = page.getByTestId("flight-generate-route-input");
        this.submitButton = page.getByTestId("flight-generate-submit-btn");
        this.errorMessage = page.getByTestId("flight-generate-error-msg");
    }

    public Locator getPageHeading() {
        return pageHeading;
    }

    public Locator getGenerateFlightsButton() {
        return generateFlightsButton;
    }

    public Locator getGenerateFlightsDrawer() {
        return generateFlightsDrawer;
    }

    public Locator getFlightGenerateForm() {
        return flightGenerateForm;
    }

    public Locator getRouteInput() {
        return routeInput;
    }

    public Locator getSubmitButton() {
        return submitButton;
    }

    public Locator getErrorMessage() {
        return errorMessage;
    }

    public AdminFlightGenerationPage clickGenerateFlightsButton() {
        generateFlightsButton.click();
        return this;
    }

    public AdminFlightGenerationPage clickSubmit() {
        submitButton.click();
        return this;
    }
}
