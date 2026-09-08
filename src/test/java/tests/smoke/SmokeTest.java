package tests.smoke;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.microsoft.playwright.assertions.PlaywrightAssertions;


@Tag("smoke")
@Tag("prod-safe")
class SmokeTest extends BaseTest {

    @Test
    void homepageLoads() {
        Locator searchButton = page.getByTestId("search-button");
        PlaywrightAssertions.assertThat(searchButton).isVisible();
    }
}