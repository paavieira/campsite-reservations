package com.paavieira.campsite.reservations.framework.controllers.dtos;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Availability {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate to;
    private final boolean available;

    public Availability(LocalDate from, LocalDate to, boolean available) {
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

    public static Availability fromDomain(com.paavieira.campsite.reservations.domain.Availability domain) {
        return new Availability(domain.getFrom(), domain.getTo(), domain.isAvailable());
    }

}
