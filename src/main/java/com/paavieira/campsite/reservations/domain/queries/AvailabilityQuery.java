package com.paavieira.campsite.reservations.domain.queries;

import java.time.LocalDate;

public class AvailabilityQuery {

    private final LocalDate from;
    private final LocalDate to;

    public AvailabilityQuery(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

}
