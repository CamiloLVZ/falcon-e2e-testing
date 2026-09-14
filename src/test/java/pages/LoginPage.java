package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {

    private final Page page;

    private final Locator title;
    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator errorLabel;

    public LoginPage(Page page) {
        this.page = page;
        emailInput = page.getByTestId("login-email-input");
        passwordInput = page.getByTestId("login-password-input");
        loginButton = page.getByTestId("login-button");
        errorLabel = page.getByTestId("login-error-label");
        title = page.getByTestId("login-title");
    }

    public Locator getTitle() {
        return title;
    }

    public void login(String email, String password){
        emailInput.fill(email);
        passwordInput.fill(password);
        loginButton.click();
    }

    public Locator getEmailInput() {
        return emailInput;
    }
    public Locator getPasswordInput() {
        return passwordInput;
    }
    public Locator getLoginButton() {
        return loginButton;
    }
    public Locator getErrorLabel() {
        return errorLabel;
    }
}
