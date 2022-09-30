package com.paavieira.campsite.reservations.domain.exceptions;


import java.time.LocalDate;

public class InvalidDuration extends RuntimeException {
    private final int errorCode = 4;
    private final LocalDate arrivalDate;
    private final LocalDate departureDate;

    public InvalidDuration(LocalDate arrivalDate, LocalDate departureDate) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    @Override
    public String getMessage() {
        return "[%03d] The reservation period is invalid: from %s to %s. The campsite can be reserved for max 3 days.".formatted(
                errorCode,
                arrivalDate,
                departureDate
        );
    }
}
