package pages;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ProfilePage extends BaseTest {

    private final Page page;
    private final Locator userBanner;
    private final Locator userName;
    private final Locator userEmail;

    public ProfilePage(Page page) {
        this.page = page;
        userBanner = page.getByTestId("user-profile-banner");
        userName = page.getByTestId("user-profile-name");
        userEmail = page.getByTestId("user-profile-email");
    }

    public Locator  getUserBanner() {
        return userBanner;
    }

    public Locator getUserName() {
        return userName;
    }

    public Locator getUserEmail() {
        return userEmail;
    }

}
