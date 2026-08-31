package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class FlightResultsPage {
        private final Page page;
        private final Locator flightCards;
        private final Locator error;

        public FlightResultsPage(Page page) {
            this.page = page;
            this.flightCards = page.getByTestId("flight-card");
            this.error = page.getByTestId("error-screen");
        }

        public Locator getFlightCards() {
            return flightCards;
        }

        public Locator getError() {
            return error;
        }
}
