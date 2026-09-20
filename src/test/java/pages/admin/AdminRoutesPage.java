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
    private final Locator createSubmitButton;

    public AdminRoutesPage(Page page) {
        this.page = page;
        this.pageHeading = page.getByTestId("routes-page-heading");
        this.createRouteButton = page.getByTestId("create-route-btn");
        this.createRouteDrawer = page.getByTestId("create-route-drawer");
        this.routeCreateForm = page.getByTestId("route-create-form");
        this.flightNumberInput = page.getByTestId("route-flight-number-input");
        this.createSubmitButton = page.getByTestId("route-create-submit-btn");
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

    public Locator getCreateSubmitButton() {
        return createSubmitButton;
    }

    public void  clickCreateRouteButton() {
        createRouteButton.click();
    }
}
