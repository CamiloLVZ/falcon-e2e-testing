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

    private final Locator registerTabButton;
    private final Locator registerTitle;
    private final Locator registerEmailInput;
    private final Locator registerPasswordInput;
    private final Locator registerConfirmPasswordInput;
    private final Locator registerButton;
    private final Locator registerErrorLabel;
    private final Locator registerSuccessLabel;

    public LoginPage(Page page) {
        this.page = page;
        emailInput = page.getByTestId("login-email-input");
        passwordInput = page.getByTestId("login-password-input");
        loginButton = page.getByTestId("login-button");
        errorLabel = page.getByTestId("login-error-label");
        title = page.getByTestId("login-title");

        registerTabButton = page.getByTestId("register-tab-button");
        registerTitle = page.getByTestId("register-title");
        registerEmailInput = page.getByTestId("register-email-input");
        registerPasswordInput = page.getByTestId("register-password-input");
        registerConfirmPasswordInput = page.getByTestId("register-confirm-password-input");
        registerButton = page.getByTestId("register-button");
        registerErrorLabel = page.getByTestId("register-error-label");
        registerSuccessLabel = page.getByTestId("register-success-label");
    }

    public Locator getTitle() {
        return title;
    }

    public void login(String email, String password){
        emailInput.fill(email);
        passwordInput.fill(password);
        loginButton.click();
    }

    public void register(String email, String password) {
        if (registerTabButton.isVisible()) {
            registerTabButton.click();
        }
        registerEmailInput.fill(email);
        registerPasswordInput.fill(password);
        registerConfirmPasswordInput.fill(password);
        registerButton.click();
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

    public Locator getRegisterTabButton() {
        return registerTabButton;
    }

    public Locator getRegisterTitle() {
        return registerTitle;
    }

    public Locator getRegisterEmailInput() {
        return registerEmailInput;
    }

    public Locator getRegisterPasswordInput() {
        return registerPasswordInput;
    }

    public Locator getRegisterConfirmPasswordInput() {
        return registerConfirmPasswordInput;
    }

    public Locator getRegisterButton() {
        return registerButton;
    }

    public Locator getRegisterErrorLabel() {
        return registerErrorLabel;
    }

    public Locator getRegisterSuccessLabel() {
        return registerSuccessLabel;
    }
}
