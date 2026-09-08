package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckInPage {
    private final Page page;
    private final Locator continueButton;
    private final Locator title;

    public CheckInPage(Page page) {
        this.page = page;
        continueButton = page.getByTestId("checkin-continue-button");
        title = page.getByTestId("checkin-title");
    }

    public Locator getTitle(){
        return title;

    }
    public Locator getContinueButton(){
        return continueButton;
    }

}
