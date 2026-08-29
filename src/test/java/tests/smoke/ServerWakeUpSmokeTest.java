package tests.smoke;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import config.TestConfig;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("smoke")
@Tag("prod-safe")
@Tag("prod-only")
    public class ServerWakeUpSmokeTest extends BaseTest {

    @Test
    void serverWakeUpShowsAndHides() {
        Assumptions.assumeTrue(TestConfig.isProdActive());

        page.navigate(TestConfig.baseUrl());
        Locator wakeUpImage = page.getByTestId("wake-up-image");
        try {
            wakeUpImage.waitFor(new Locator.WaitForOptions().setTimeout(5000));
        } catch (TimeoutError e) {
            Assumptions.assumeTrue(false, "Backend already up, could not verify cold start");
        }

        PlaywrightAssertions.assertThat(wakeUpImage)
                .isHidden(new LocatorAssertions.IsHiddenOptions().setTimeout(300000));

        Locator searchButton = page.getByTestId("search-button");
        PlaywrightAssertions.assertThat(searchButton).isVisible();
    }
}
