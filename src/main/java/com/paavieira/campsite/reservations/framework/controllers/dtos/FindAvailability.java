package com.paavieira.campsite.reservations.framework.controllers.dtos;

import com.paavieira.campsite.reservations.framework.validations.ValidPeriod;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

@Valid
@ValidPeriod
public class FindAvailability {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate to;

    public FindAvailability(LocalDate from) {
        this.from = from;
        this.to = null;
    }

    public FindAvailability(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public LocalDate getToOrDefault() {
        return Optional.ofNullable(to).orElse(from.plusMonths(1L));
    }

    public boolean isValidPeriod() {
        return Optional.ofNullable(to).map(from::isBefore).orElse(true);
    }
}
