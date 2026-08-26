package base;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SmokeTest extends BaseTest {

    @Test
    void homepageLoads() {
        page.navigate("https://falconbooking.org");
        assertThat(page.title()).isNotEmpty();
    }
}