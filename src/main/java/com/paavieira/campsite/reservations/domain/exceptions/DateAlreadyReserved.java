package com.paavieira.campsite.reservations.domain.exceptions;

import java.time.LocalDate;

public class DateAlreadyReserved extends RuntimeException {
    private final int errorCode = 1;

    private final LocalDate date;

    public DateAlreadyReserved(LocalDate date) {
        this.date = date;
    }

    @Override
    public String getMessage() {
        return "[%03d] The selected date is already reserved: %s.".formatted(errorCode, date);
    }
}
