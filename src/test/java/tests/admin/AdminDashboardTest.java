package tests.admin;

import base.BaseTest;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.AdminDashboardPage;
import pages.admin.AdminFlightGenerationPage;
import pages.admin.AdminRoutesPage;
import pages.admin.AdminSidebarPage;

import java.util.concurrent.ThreadLocalRandom;

@Tag("regression")
@Tag("admin")
public class AdminDashboardTest extends BaseTest {

    @Test
    public void adminDashboardNavigation() {
        loginAs(ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD);

        AdminDashboardPage dashboardPage = new AdminDashboardPage(page);
        PlaywrightAssertions.assertThat(dashboardPage.getSidebarTitle()).isVisible();

        AdminSidebarPage sidebar = new AdminSidebarPage(page);
        PlaywrightAssertions.assertThat(sidebar.getHeader()).isVisible();
        PlaywrightAssertions.assertThat(sidebar.getNav()).isVisible();
        PlaywrightAssertions.assertThat(sidebar.getRoutesLink()).isVisible();
        PlaywrightAssertions.assertThat(sidebar.getFlightGenerationLink()).isVisible();
        PlaywrightAssertions.assertThat(sidebar.getFlightsLink()).isVisible();
        PlaywrightAssertions.assertThat(sidebar.getReservationsLink()).isVisible();
    }

    @Test
    public void adminGeneratesFlights() {
        loginAs(ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD);

        AdminFlightGenerationPage flightGenPage = new AdminSidebarPage(page)
                .clickFlightGeneration();

        PlaywrightAssertions.assertThat(flightGenPage.getPageHeading()).hasText("Generación de Vuelos");
        PlaywrightAssertions.assertThat(flightGenPage.getGenerateFlightsButton()).isVisible();

        flightGenPage.clickGenerateFlightsButton();

        PlaywrightAssertions.assertThat(flightGenPage.getGenerateFlightsDrawer()).isVisible();
        PlaywrightAssertions.assertThat(flightGenPage.getFlightGenerateForm()).isVisible();

        flightGenPage.clickSubmit();

        PlaywrightAssertions.assertThat(flightGenPage.getErrorMessage()).isHidden();
    }

    @Test
    public void adminCreatesRoute() {
        loginAs(ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD);

        AdminRoutesPage routesPage = new AdminSidebarPage(page)
                .clickRoutes();

        PlaywrightAssertions.assertThat(routesPage.getPageHeading()).hasText("Rutas");
        PlaywrightAssertions.assertThat(routesPage.getCreateRouteButton()).isVisible();

        routesPage.clickCreateRouteButton();

        PlaywrightAssertions.assertThat(routesPage.getCreateRouteDrawer()).isVisible();

        String randomFlightNumber = "AV" + ThreadLocalRandom.current().nextInt(1000, 9999);

        routesPage.fillRouteForm(randomFlightNumber, "BOG", "MDE", "1", "60", "150000", "300000");
        routesPage.clickSubmit();

        PlaywrightAssertions.assertThat(routesPage.getErrorMessage()).isHidden();
        PlaywrightAssertions.assertThat(routesPage.getSuccessMessage()).isVisible();
    }

    @Test
    public void adminCreatesRouteSameOriginAndDestinationFails() {
        loginAs(ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD);

        AdminRoutesPage routesPage = new AdminSidebarPage(page)
                .clickRoutes();

        routesPage.clickCreateRouteButton();

        PlaywrightAssertions.assertThat(routesPage.getCreateRouteDrawer()).isVisible();

        String randomFlightNumber = "AV" + ThreadLocalRandom.current().nextInt(1000, 9999);

        routesPage.fillRouteForm(randomFlightNumber, "BOG", "BOG", "1", "60", "150000", "300000");
        routesPage.clickSubmit();

        PlaywrightAssertions.assertThat(routesPage.getErrorMessage()).isVisible();
    }
}
