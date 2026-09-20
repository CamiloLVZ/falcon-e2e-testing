package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AdminDashboardPage {

    private final Locator sidebarTitle;

    public AdminDashboardPage(Page page) {
        this.sidebarTitle = page.getByTestId("admin-sidebar-header");
    }

    public Locator getSidebarTitle() {
        return sidebarTitle;
    }
}
