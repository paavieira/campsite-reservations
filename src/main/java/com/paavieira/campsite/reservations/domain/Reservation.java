package com.paavieira.campsite.reservations.domain;

import com.paavieira.campsite.reservations.domain.exceptions.InvalidArrivalDate;
import com.paavieira.campsite.reservations.domain.exceptions.InvalidDepartureDate;
import com.paavieira.campsite.reservations.domain.exceptions.InvalidDuration;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Reservation {
    private final String bookingIdentifier;

    private final LocalDate creationDate;
    private final LocalDate arrivalDate;
    private final LocalDate departureDate;
    private final ReservationStatus status;
    private final User user;

    private Reservation(String bookingIdentifier,
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

    public static Reservation create(String bookingIdentifier,
                                     LocalDate creationDate,
                                     LocalDate arrivalDate,
                                     LocalDate departureDate,
                                     ReservationStatus status,
                                     User user) {
        if (departureDate.isBefore(arrivalDate) || departureDate.isEqual(arrivalDate)) {
            throw new InvalidDepartureDate(arrivalDate, departureDate);
        }

        if (arrivalDate.until(departureDate, ChronoUnit.DAYS) > 3L) {
            throw new InvalidDuration(arrivalDate, departureDate);
        }

        if (creationDate.until(arrivalDate, ChronoUnit.DAYS) < 1L) {
            throw new InvalidArrivalDate(arrivalDate);
        }

        if (creationDate.until(arrivalDate, ChronoUnit.MONTHS) > 1L) {
            throw new InvalidArrivalDate(arrivalDate);
        }

        return new Reservation(bookingIdentifier, creationDate, arrivalDate, departureDate, status, user);
    }

    public Reservation modify(LocalDate arrivalDate, LocalDate departureDate) {
        return Reservation.create(bookingIdentifier, creationDate, arrivalDate, departureDate, status, user);
    }

    public Reservation cancel() {
        return new Reservation(bookingIdentifier, creationDate, arrivalDate, departureDate, ReservationStatus.CANCELLED, user);
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

    public Long getDuration() {
        return arrivalDate.until(departureDate, ChronoUnit.DAYS);
    }

    public boolean isCancelled() {
        return status == ReservationStatus.CANCELLED;
    }

    public List<ReservedDate> getReservedDates() {
        return isCancelled()
                ? Collections.emptyList()
                : LongStream.range(0, getDuration())
                            .boxed()
                            .map(offset -> arrivalDate.plusDays(offset))
                            .map(date -> new ReservedDate(date, bookingIdentifier))
                            .collect(Collectors.toList());
    }

}
