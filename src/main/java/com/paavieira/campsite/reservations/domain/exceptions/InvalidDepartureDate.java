package com.paavieira.campsite.reservations.domain.exceptions;

import java.time.LocalDate;

public class InvalidDepartureDate extends RuntimeException {
    private final int errorCode = 3;
    private final LocalDate arrivalDate;
    private final LocalDate departureDate;

    public InvalidDepartureDate(LocalDate arrivalDate, LocalDate departureDate) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    @Override
    public String getMessage() {
        return "[%03d] The departure date is invalid: %s. The departure date must be after the arrival date: %s.".formatted(errorCode,
                                                                                                                            departureDate,
                                                                                                                            arrivalDate);
    }
}
