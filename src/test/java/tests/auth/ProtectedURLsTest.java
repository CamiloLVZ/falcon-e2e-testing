package tests.auth;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import config.TestConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.AdminDashboardPage;
import pages.LoginPage;
import pages.ProfilePage;

@Tag("regression")
public class ProtectedURLsTest extends BaseTest {

    @Test
    public void clientTriesToAccessAdminDashboard(){
        loginAs(CLIENT_USER_EMAIL, CLIENT_USER_PASSWORD);

        ProfilePage profilePage = new ProfilePage(page);
        PlaywrightAssertions.assertThat(profilePage.getUserBanner()).isVisible();

        navigateSafely(TestConfig.baseUrl()+"/admin");
        Locator accessRestrictedLabel = page.getByTestId("access-restricted-label");
        PlaywrightAssertions.assertThat(accessRestrictedLabel).isVisible();
    }

    @Test
    public void adminAccessesAdminDashboard(){
        loginAs(ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD);

        AdminDashboardPage adminDashboardPage = new AdminDashboardPage(page);
        Locator accessRestrictedLabel = page.getByTestId("access-restricted-label");

        PlaywrightAssertions.assertThat(accessRestrictedLabel).isHidden();
        PlaywrightAssertions.assertThat(adminDashboardPage.getSidebarTitle()).isVisible();

    }

    @Tag("prod-safe")
    @Test
    public void guestAccessesProfile(){
        navigateSafely(TestConfig.baseUrl()+"/profile");

        LoginPage loginPage = new LoginPage(page);
        PlaywrightAssertions.assertThat(loginPage.getTitle()).isVisible();
    }

}
