package com.paavieira.campsite.reservations.domain.commands;

public class CancelReservationCommand {
    private final String bookingIdentifier;

    public CancelReservationCommand(String bookingIdentifier) {
        this.bookingIdentifier = bookingIdentifier;
    }

    public String getBookingIdentifier() {
        return bookingIdentifier;
    }
}
