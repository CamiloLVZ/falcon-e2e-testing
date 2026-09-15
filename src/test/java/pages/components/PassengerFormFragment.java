package pages.components;

import com.microsoft.playwright.Locator;

public class PassengerFormFragment {
    private final Locator root;
    private final Locator firstNameInput;
    private final Locator lastNameInput;
    private final Locator dateOfBirthInput;
    private final Locator identificationInput;
    private final Locator nationalitySelect;

    public PassengerFormFragment(Locator root) {
        this.root = root;
        this.firstNameInput = root.getByTestId("passenger-first-name-input");
        this.lastNameInput = root.getByTestId("passenger-last-name-input");
        this.dateOfBirthInput = root.getByTestId("passenger-date-of-birth-input");
        this.identificationInput = root.getByTestId("passenger-identification-number-input");
        this.nationalitySelect = root.getByTestId("passenger-nationality-select");
    }

    public void fill(String firstName, String lastName, String dob, String identificationNumber, String nationality) {
        firstNameInput.fill(firstName);
        lastNameInput.fill(lastName);
        dateOfBirthInput.fill(dob);
        identificationInput.fill(identificationNumber);
        nationalitySelect.selectOption(nationality);
    }

    public Locator getFirstNameInput() { return firstNameInput; }

    public Locator getLastNameInput() {
        return lastNameInput;
    }

    public Locator getDateOfBirthInput() {
        return dateOfBirthInput;
    }

    public Locator getNationalitySelect() {
        return nationalitySelect;
    }

    public Locator getIdentificationInput() {
        return identificationInput;
    }
}