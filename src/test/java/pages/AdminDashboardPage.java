package pages;

import base.BaseTest;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class AdminDashboardPage extends BaseTest {

    private final Page page;
    private final Locator sidebarTitle;

    public AdminDashboardPage(Page page) {
        this.page = page;
        sidebarTitle = page.getByTestId("admin-sidebar-title");
    }

    public Locator getSidebarTitle() {
        return sidebarTitle;
    }

}
