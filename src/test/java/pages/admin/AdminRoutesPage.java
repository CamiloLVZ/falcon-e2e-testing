package pages.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AdminRoutesPage {

    private final Page page;

    private final Locator pageHeading;
    private final Locator createRouteButton;
    private final Locator createRouteDrawer;
    private final Locator routeCreateForm;
    private final Locator flightNumberInput;
    private final Locator originSelect;
    private final Locator destinationSelect;
    private final Locator airplaneTypeSelect;
    private final Locator durationInput;
    private final Locator priceEconomyInput;
    private final Locator priceFirstClassInput;
    private final Locator createSubmitButton;
    private final Locator errorMessage;
    private final Locator successMessage;

    public AdminRoutesPage(Page page) {
        this.page = page;
        this.pageHeading = page.getByTestId("routes-page-heading");
        this.createRouteButton = page.getByTestId("create-route-btn");
        this.createRouteDrawer = page.getByTestId("create-route-drawer");
        this.routeCreateForm = page.getByTestId("route-create-form");
        this.flightNumberInput = page.getByTestId("route-flight-number-input");
        this.originSelect = page.getByTestId("route-origin-select");
        this.destinationSelect = page.getByTestId("route-destination-select");
        this.airplaneTypeSelect = page.getByTestId("route-airplane-type-select");
        this.durationInput = page.getByTestId("route-duration-input");
        this.priceEconomyInput = page.getByTestId("route-price-economy-input");
        this.priceFirstClassInput = page.getByTestId("route-price-firstclass-input");
        this.createSubmitButton = page.getByTestId("route-create-submit-btn");
        this.errorMessage = page.getByTestId("route-create-error-msg");
        this.successMessage = page.getByTestId("route-create-success-msg");
    }

    public Locator getPageHeading() {
        return pageHeading;
    }

    public Locator getCreateRouteButton() {
        return createRouteButton;
    }

    public Locator getCreateRouteDrawer() {
        return createRouteDrawer;
    }

    public Locator getRouteCreateForm() {
        return routeCreateForm;
    }

    public Locator getFlightNumberInput() {
        return flightNumberInput;
    }

    public Locator getOriginSelect() {
        return originSelect;
    }

    public Locator getDestinationSelect() {
        return destinationSelect;
    }

    public Locator getAirplaneTypeSelect() {
        return airplaneTypeSelect;
    }

    public Locator getDurationInput() {
        return durationInput;
    }

    public Locator getPriceEconomyInput() {
        return priceEconomyInput;
    }

    public Locator getPriceFirstClassInput() {
        return priceFirstClassInput;
    }

    public Locator getCreateSubmitButton() {
        return createSubmitButton;
    }

    public Locator getErrorMessage() {
        return errorMessage;
    }

    public Locator getSuccessMessage() {
        return successMessage;
    }

    public AdminRoutesPage clickCreateRouteButton() {
        createRouteButton.click();
        return this;
    }

    public AdminRoutesPage fillRouteForm(String flightNumber, String originIata, String destIata, String airplaneTypeId, String duration, String priceEconomy, String priceFirst) {
        flightNumberInput.fill(flightNumber);
        originSelect.selectOption(originIata);
        destinationSelect.selectOption(destIata);
        airplaneTypeSelect.selectOption(airplaneTypeId);
        durationInput.fill(duration);
        priceEconomyInput.fill(priceEconomy);
        priceFirstClassInput.fill(priceFirst);
        return this;
    }

    public AdminRoutesPage clickSubmit() {
        createSubmitButton.click();
        return this;
    }
}
