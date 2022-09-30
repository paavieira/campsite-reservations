package com.paavieira.campsite.reservations.framework.controllers.dtos;

import com.paavieira.campsite.reservations.domain.ReservationStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Reservation {

    private final String bookingIdentifier;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate creationDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate arrivalDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate departureDate;
    private final ReservationStatus status;
    private final User user;

    public Reservation(String bookingIdentifier,
                       LocalDate creationDate,
                       LocalDate arrivalDate,
                       LocalDate departureDate,
                       ReservationStatus status,
                       User user) {
        this.bookingIdentifier = bookingIdentifier;
        this.creationDate = creationDate;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.status = status;
        this.user = user;
    }

    public String getBookingIdentifier() {
        return bookingIdentifier;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public static Reservation fromDomain(com.paavieira.campsite.reservations.domain.Reservation domain) {
        return new Reservation(
                domain.getBookingIdentifier(),
                domain.getCreationDate(),
                domain.getArrivalDate(),
                domain.getDepartureDate(),
                domain.getStatus(),
                User.fromDomain(domain.getUser())
        );
    }

}
