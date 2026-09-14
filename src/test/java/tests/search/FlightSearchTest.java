package tests.search;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.FlightResultsPage;
import pages.HomePage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Tag("prod-safe")
@Tag("regression")
public class FlightSearchTest extends BaseTest {

    @Test
    public void searchFlights() {
        LocalDate date = LocalDate.now().plusDays(4);
        FlightResultsPage results = new HomePage(page)
                .searchFlights(
                        "BOG",
                        "VVC",
                        date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        PlaywrightAssertions.assertThat(results.getError()).isHidden();
        PlaywrightAssertions.assertThat(results.getFlightCards().first()).isVisible();
    }

    @Test
    public void searchFlightsNoResults() {
        LocalDate date = LocalDate.now().plusYears(2);
        FlightResultsPage results = new HomePage(page)
                .searchFlights(
                        "BOG",
                        "VVC",
                        date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        PlaywrightAssertions.assertThat(results.getFlightCards().first()).isHidden();
        PlaywrightAssertions.assertThat(results.getError()).isVisible();
    }

    @Tag("debug")
    @Test
    public void bookButton(){
        LocalDate date = LocalDate.now().plusDays(5);
        FlightResultsPage results = new HomePage(page)
                .searchFlights(
                        "BOG",
                        "CLO",
                        date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        results.getBookFlightButtons().first().click();
        Locator bookingTitle = page.getByTestId("booking-page-title");
        PlaywrightAssertions.assertThat(bookingTitle).isVisible();
    }
}
