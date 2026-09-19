package fixtures;

public record Reservation(
        String reservationNumber,
        String contactEmail,
        String identificationNumber,
        String countryIso
) {}