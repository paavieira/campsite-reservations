package com.paavieira.campsite.reservations.domain.exceptions;

public class ReservationNotFound extends RuntimeException {
    private final int errorCode = 6;
    private final String bookingIdentifier;

    public ReservationNotFound(String bookingIdentifier) {
        this.bookingIdentifier = bookingIdentifier;
    }

    @Override
    public String getMessage() {
        return "[%03d] Reservation not found: %s.".formatted(errorCode, bookingIdentifier);
    }

}
