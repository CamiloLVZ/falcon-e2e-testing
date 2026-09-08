package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;
    private final Locator loginButton;
    private final Locator title;

    public LoginPage(Page page) {
        this.page = page;
        loginButton = page.getByTestId("login-button");
        title = page.getByTestId("login-title");
    }

    public Locator getTitle() {
        return title;
    }

    public Locator getLoginButton() {
        return loginButton;
    }
}
