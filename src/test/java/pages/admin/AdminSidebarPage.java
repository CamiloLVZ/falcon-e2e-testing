package pages.admin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AdminSidebarPage {

    private final Page page;

    private final Locator header;
    private final Locator nav;
    private final Locator aircraftLink;
    private final Locator locationsLink;
    private final Locator routesLink;
    private final Locator flightsLink;
    private final Locator flightGenerationLink;
    private final Locator reservationsLink;
    private final Locator passengersLink;
    private final Locator usersLink;
    private final Locator checkInLink;
    private final Locator boardingLink;

    public AdminSidebarPage(Page page) {
        this.page = page;
        this.header = page.getByTestId("admin-sidebar-header");
        this.nav = page.getByTestId("admin-sidebar-nav");
        this.aircraftLink = page.getByTestId("admin-nav-aircraft");
        this.locationsLink = page.getByTestId("admin-nav-locations");
        this.routesLink = page.getByTestId("admin-nav-routes");
        this.flightsLink = page.getByTestId("admin-nav-flights");
        this.flightGenerationLink = page.getByTestId("admin-nav-flight-generation");
        this.reservationsLink = page.getByTestId("admin-nav-reservations");
        this.passengersLink = page.getByTestId("admin-nav-passengers");
        this.usersLink = page.getByTestId("admin-nav-users");
        this.checkInLink = page.getByTestId("admin-nav-checkin");
        this.boardingLink = page.getByTestId("admin-nav-boarding");
    }

    public Locator getHeader() {
        return header;
    }

    public Locator getNav() {
        return nav;
    }

    public Locator getAircraftLink() {
        return aircraftLink;
    }

    public Locator getLocationsLink() {
        return locationsLink;
    }

    public Locator getRoutesLink() {
        return routesLink;
    }

    public Locator getFlightsLink() {
        return flightsLink;
    }

    public Locator getFlightGenerationLink() {
        return flightGenerationLink;
    }

    public Locator getReservationsLink() {
        return reservationsLink;
    }

    public Locator getPassengersLink() {
        return passengersLink;
    }

    public Locator getUsersLink() {
        return usersLink;
    }

    public Locator getCheckInLink() {
        return checkInLink;
    }

    public Locator getBoardingLink() {
        return boardingLink;
    }

    public AdminRoutesPage clickRoutes() {
        routesLink.click();
        return new AdminRoutesPage(page);
    }

    public AdminFlightGenerationPage clickFlightGeneration() {
        flightGenerationLink.click();
        return new AdminFlightGenerationPage(page);
    }
}
