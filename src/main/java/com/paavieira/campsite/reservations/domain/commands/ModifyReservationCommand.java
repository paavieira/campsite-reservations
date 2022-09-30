package com.paavieira.campsite.reservations.domain.commands;

import java.time.LocalDate;

public class ModifyReservationCommand {
    private final String bookingIdentifier;
    private final LocalDate arrivalDate;
    private final LocalDate departureDate;

    public ModifyReservationCommand(String bookingIdentifier, LocalDate arrivalDate, LocalDate departureDate) {
        this.bookingIdentifier = bookingIdentifier;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public String getBookingIdentifier() {
        return bookingIdentifier;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }
}
