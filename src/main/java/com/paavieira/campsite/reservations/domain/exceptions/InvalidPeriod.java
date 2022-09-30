package com.paavieira.campsite.reservations.domain.exceptions;


import java.time.LocalDate;

public class InvalidPeriod extends RuntimeException {
    private final int errorCode = 5;
    private final LocalDate from;
    private final LocalDate to;

    public InvalidPeriod(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String getMessage() {
        return "[%03d] The period is invalid: from %s to %s.".formatted(
                errorCode,
                from,
                to
        );
    }
}
