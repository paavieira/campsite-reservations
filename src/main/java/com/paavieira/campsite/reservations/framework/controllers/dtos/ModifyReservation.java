package com.paavieira.campsite.reservations.framework.controllers.dtos;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Valid
public class ModifyReservation {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate arrivalDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate departureDate;

    public ModifyReservation(LocalDate arrivalDate, LocalDate departureDate) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }
}