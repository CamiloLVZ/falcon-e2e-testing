package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage{

    private final Page page;
    private final Locator originInput;
    private final Locator destinationInput;
    private final Locator dateInput;
    private final Locator searchButton;

    public HomePage(Page page){
        this.page = page;
        this.originInput = page.getByTestId("search-bar-origin-input");
        this.destinationInput = page.getByTestId("search-bar-destination-input");
        this.dateInput = page.getByTestId("search-bar-date-input");
        this.searchButton = page.getByTestId("search-bar-button");
    }

    public FlightResultsPage searchFlights(String originAirport, String destinationAirport, String date){
        selectAirport(originInput, originAirport);
        selectAirport(destinationInput, destinationAirport);
        dateInput.fill(date);
        searchButton.click();

        return new FlightResultsPage(page);
    }

    private void selectAirport(Locator input, String airportCode){
        input.fill(airportCode);
        page.getByTestId("airport-option-"+airportCode).click();
    }

    public Locator getSearchButton(){
        return searchButton;
    }

}
