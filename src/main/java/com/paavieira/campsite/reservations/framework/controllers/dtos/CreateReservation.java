package com.paavieira.campsite.reservations.framework.controllers.dtos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Valid
public class CreateReservation {

    @NotNull
    @Valid
    private final User user;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate arrivalDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate departureDate;

    public CreateReservation(User user, LocalDate arrivalDate, LocalDate departureDate) {
        this.user = user;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
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