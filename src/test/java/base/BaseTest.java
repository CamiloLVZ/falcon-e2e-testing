package base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import config.TestConfig;
import org.junit.jupiter.api.*;

public abstract class BaseTest {

    static Playwright playwright;
    static Browser browser;
    protected BrowserContext context;
    protected Page page;

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
        page.navigate(TestConfig.baseUrl());

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

    @AfterEach
    void closeContext() {
        context.close();
    }
}
