package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import pages.booking.BookingPage;

public class FlightResultsPage {
        private final Page page;
        private final Locator flightCards;
        private final Locator bookFlightButtons;
        private final Locator error;

        public FlightResultsPage(Page page) {
            this.page = page;
            this.flightCards = page.getByTestId("flight-card");
            this.error = page.getByTestId("error-screen");
            this.bookFlightButtons = page.getByTestId("book-flight-button");
        }

        public Locator getFlightCards() {
            return flightCards;
        }

        public Locator getError() {
            return error;
        }

        public BookingPage clickBookFlight(int index) {
            bookFlightButtons.nth(index).click();
            return new BookingPage(page);
        }
}
