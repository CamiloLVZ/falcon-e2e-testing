package base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import config.TestConfig;
import org.junit.jupiter.api.*;
import pages.LoginPage;

public abstract class BaseTest {

    static Playwright playwright;
    static Browser browser;
    protected BrowserContext context;
    protected Page page;

    protected final String CLIENT_USER_EMAIL="e2e.client@falcon.test";
    protected final String CLIENT_USER_PASSWORD="Test1234!";
    protected final String ADMIN_USER_EMAIL="e2e.admin@falcon.test";
    protected final String ADMIN_USER_PASSWORD="Admin1234!";

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));
    }

    @AfterAll
    static void closeBrowser() {
        playwright.close();
    }

    @BeforeEach
    void createContextPageAndNavigate() {
        context = browser.newContext();
        page = context.newPage();
        this.navigateSafely(TestConfig.baseUrl());
    }

    protected void navigateSafely(String url){
        page.navigate(url);

        if (!TestConfig.isLocalActive()) {
            Locator wakeUpImage = page.getByTestId("wake-up-image");

            try {
                wakeUpImage.waitFor(new Locator.WaitForOptions().setTimeout(5000));
            } catch (TimeoutError e) {
                return;
            }
            PlaywrightAssertions.assertThat(wakeUpImage)
                    .isHidden(new LocatorAssertions.IsHiddenOptions().setTimeout(300000));
        }
    }

    protected void loginAs(String email, String password){
        this.navigateSafely(TestConfig.baseUrl()+"/login");
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(email, password);
    }

    @AfterEach
    void closeContext() {
        context.close();
    }
}
