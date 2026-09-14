package tests.navigation;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import config.TestConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.*;

@Tag("regression")
@Tag("prod-safe")
public class NavBarTest extends BaseTest {

    @Test
    public void navigateToBooking(){
        navigateSafely(TestConfig.baseUrl() + "/manage");
        Locator bookNavButton = page.getByTestId("navlink-reservar");
        bookNavButton.click();
        HomePage homePage = new HomePage(page);

        PlaywrightAssertions.assertThat(homePage.getSearchButton()).isVisible();
    }

    @Test
    public void navigateToManage(){
        Locator manageNavButton = page.getByTestId("navlink-gestionar");
        manageNavButton.click();
        ManagePage managePage = new ManagePage(page);

        PlaywrightAssertions.assertThat(managePage.getTitle()).isVisible();
    }

    @Test
    public void navigateToCheckIn(){
        Locator checkInNavButton = page.getByTestId("navlink-check-in");
        checkInNavButton.click();
        CheckInPage checkInPage = new CheckInPage(page);

        PlaywrightAssertions.assertThat(checkInPage.getTitle()).isVisible();
    }

    @Test
    public void navigateToBoarding(){
        Locator boardingNavButton = page.getByTestId("navlink-abordaje");
        boardingNavButton.click();
        BoardingPage boardingPage = new BoardingPage(page);

        PlaywrightAssertions.assertThat(boardingPage.getTitle()).isVisible();
    }

    @Test
    public void navigateToLogin(){
        Locator loginNavButton = page.getByTestId("navlink-login");
        loginNavButton.click();
        LoginPage loginPage = new LoginPage(page);

        PlaywrightAssertions.assertThat(loginPage.getTitle()).isVisible();
    }

}
