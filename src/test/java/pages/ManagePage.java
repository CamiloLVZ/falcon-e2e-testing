package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ManagePage {
    private final Page page;
    private final Locator searchButton;
    private final Locator title;

    public ManagePage(Page page) {
        this.page = page;
        searchButton = page.getByTestId("search-reservation-button");
        title = page.getByTestId("manage-title");
    }

    public Locator getTitle(){
        return title;

    }
    public Locator getSearchButton(){
        return searchButton;
    }

}
