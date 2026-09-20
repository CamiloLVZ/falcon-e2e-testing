package fixtures;

import java.util.List;

public class BookingRequest {
    private String flightId;
    private String contactEmail;
    private List<PassengerItem> passengers;

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public List<PassengerItem> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerItem> passengers) {
        this.passengers = passengers;
    }

    public static class PassengerItem {
        private PassengerDetails passenger;
        private String seatClass;
        private double unitPrice;

        public PassengerDetails getPassenger() {
            return passenger;
        }

        public void setPassenger(PassengerDetails passenger) {
            this.passenger = passenger;
        }

        public String getSeatClass() {
            return seatClass;
        }

        public void setSeatClass(String seatClass) {
            this.seatClass = seatClass;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }
    }

    public static class PassengerDetails {
        private String firstName;
        private String lastName;
        private String gender;
        private String nationalityIsoCode;
        private String dateOfBirth;
        private String passportNumber;
        private String identificationNumber;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNationalityIsoCode() {
            return nationalityIsoCode;
        }

        public void setNationalityIsoCode(String nationalityIsoCode) {
            this.nationalityIsoCode = nationalityIsoCode;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getPassportNumber() {
            return passportNumber;
        }

        public void setPassportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
        }

        public String getIdentificationNumber() {
            return identificationNumber;
        }

        public void setIdentificationNumber(String identificationNumber) {
            this.identificationNumber = identificationNumber;
        }

    }
}