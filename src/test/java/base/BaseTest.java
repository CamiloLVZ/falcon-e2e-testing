package base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.RequestOptions;
import config.TestConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import pages.LoginPage;

import java.util.concurrent.ThreadLocalRandom;

public abstract class BaseTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    protected final String CLIENT_USER_EMAIL = "e2e.client@falcon.test";
    protected final String CLIENT_USER_PASSWORD = "Test1234!";
    protected final String ADMIN_USER_EMAIL = "e2e.admin@falcon.test";
    protected final String ADMIN_USER_PASSWORD = "Admin1234!";

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

    @AfterEach
    void closeContext() {
        context.close();
    }

    protected void navigateSafely(String url) {
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

    protected void loginAs(String email, String password) {
        this.navigateSafely(TestConfig.baseUrl() + "/login");
        LoginPage loginPage = new LoginPage(page);
        loginPage.login(email, password);
    }

    protected String getClientJWT() {
        APIRequestContext request = playwright.request().newContext();
        String loginJson = "{\"email\":\"e2e.client@falcon.test\",\"password\":\"Test1234!\"}";

        APIResponse response = request.post(
                TestConfig.apiBaseUrl() + "/v1/auth/login",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(loginJson)
        );

        PlaywrightAssertions.assertThat(response).isOK();

        String token;
        try {
            JsonNode responseBody = new ObjectMapper().readTree(response.text());
            token = responseBody.get("accessToken").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing login response token", e);
        }
        return token;

    }

    protected String generateRandomIdentification() {
        long id = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        return String.valueOf(id);
    }

    protected String intPriceToString(Integer price) {
        if (price == null) {
            return "$0";
        }

        String sign = price < 0 ? "-$" : "$";
        String value = String.valueOf(Math.abs(price));
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {
            if (i > 0 && (value.length() - i) % 3 == 0) {
                formatted.append('.');
            }
            formatted.append(value.charAt(i));
        }

        return sign + formatted;
    }

    protected Integer stringPriceToInteger(String price) {
        return Integer.parseInt(price.replaceAll("[^0-9]", ""));
    }

}
