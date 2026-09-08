package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class BoardingPage {
    private final Page page;
    private final Locator scanQRButton;
    private final Locator title;

    public BoardingPage(Page page) {
        this.page = page;
        scanQRButton = page.getByTestId("scan-qr-button");
        title = page.getByTestId("boarding-title");
    }

    public Locator getTitle() {
        return title;
    }

    public Locator getScanQRButton() {
        return scanQRButton;
    }

}
