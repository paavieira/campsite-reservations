package com.paavieira.campsite.reservations.domain;

import com.paavieira.campsite.reservations.domain.exceptions.InvalidPeriod;

import java.time.LocalDate;

public class Availability {
    private final LocalDate from;
    private final LocalDate to;
    private final boolean available;

    private Availability(LocalDate from, LocalDate to, boolean available) {
        this.from = from;
        this.to = to;
        this.available = available;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public boolean isAvailable() {
        return available;
    }

    public static Availability available(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new InvalidPeriod(from, to);
        } else {
            return new Availability(from, to, true);
        }
    }

    public static Availability unavailable(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new InvalidPeriod(from, to);
        } else {
            return new Availability(from, to, false);
        }
    }
}
