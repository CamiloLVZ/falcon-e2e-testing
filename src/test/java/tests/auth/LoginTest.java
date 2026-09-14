package tests.auth;

import base.BaseTest;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.AdminDashboardPage;
import pages.LoginPage;
import pages.ProfilePage;

@Tag("regression")
public class LoginTest extends BaseTest {

    @Test
    public void clientLogin() {
        loginAs(CLIENT_USER_EMAIL, CLIENT_USER_PASSWORD);

        ProfilePage profilePage = new ProfilePage(page);
        PlaywrightAssertions.assertThat(profilePage.getUserBanner()).isVisible();
        PlaywrightAssertions.assertThat(profilePage.getUserEmail()).containsText(CLIENT_USER_EMAIL);
    }

    @Test
    public void adminLogin() {
        loginAs(ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD);

        AdminDashboardPage adminDashboardPage = new AdminDashboardPage(page);
        PlaywrightAssertions.assertThat(adminDashboardPage.getSidebarTitle()).isVisible();
    }

    @Test
    public void invalidLogin() {
        loginAs("not-valid@email.com", "incorrect-password");
        LoginPage loginPage = new LoginPage(page);
        PlaywrightAssertions.assertThat(loginPage.getErrorLabel()).isVisible();
    }
}
