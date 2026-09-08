package base;

import com.microsoft.playwright.*;
import config.TestConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

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
    }

        @AfterEach
    void closeContext() {
        context.close();
    }
}
