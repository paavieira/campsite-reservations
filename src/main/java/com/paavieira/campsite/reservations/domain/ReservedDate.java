package com.paavieira.campsite.reservations.domain;

import java.time.LocalDate;

public class ReservedDate {
    private final LocalDate date;
    private final String bookingIdentifier;

    public ReservedDate(LocalDate date, String bookingIdentifier) {
        this.date = date;
        this.bookingIdentifier = bookingIdentifier;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getBookingIdentifier() {
        return bookingIdentifier;
    }
}
