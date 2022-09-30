package com.paavieira.campsite.reservations.domain.commands;

import com.paavieira.campsite.reservations.domain.User;

import java.time.LocalDate;

public class CreateReservationCommand {
    private final LocalDate createdDate;
    private final String bookingIdentifier;
    private final User user;
    private final LocalDate arrivalDate;
    private final LocalDate departureDate;

    public CreateReservationCommand(LocalDate createdDate,
                                    String bookingIdentifier,
                                    User user,
                                    LocalDate arrivalDate,
                                    LocalDate departureDate) {
        this.createdDate = createdDate;
        this.bookingIdentifier = bookingIdentifier;
        this.user = user;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getBookingIdentifier() {
        return bookingIdentifier;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }
}
