package pages.components;

import com.microsoft.playwright.Locator;

public class SeatSelectionComponent {

    private final Locator root;
    private final Locator confirmSeatButton;
    private final Locator cancelButton;

    public SeatSelectionComponent(Locator root) {
        this.root = root;
        this.confirmSeatButton = root.getByTestId("seat-selection-confirm-button");
        this.cancelButton = root.getByTestId("seat-selection-cancel-seat-button");
    }

    public void selectFirstAvailableSeat() {
        Locator availableSeats = root.locator("button[data-testid='seat-button']:enabled");
        availableSeats.first().click();
        confirmSelection();
    }

    public void confirmSelection() {
        confirmSeatButton.click();
    }
}