package com.paavieira.campsite.reservations.domain.exceptions;

import java.time.LocalDate;

public class InvalidArrivalDate extends RuntimeException {
    private final int errorCode = 2;
    private final LocalDate arrivalDate;

    public InvalidArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Override
    public String getMessage() {
        return "[%03d] The arrival date is invalid: %s. The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.".formatted(
                errorCode, arrivalDate);
    }
}
