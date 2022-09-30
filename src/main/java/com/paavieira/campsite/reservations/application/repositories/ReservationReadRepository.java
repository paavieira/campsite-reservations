package com.paavieira.campsite.reservations.application.repositories;

import com.paavieira.campsite.reservations.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationReadRepository {
    Reservation findActive(String bookingIdentifier);

    List<Reservation> listActive(LocalDate from, LocalDate to);
}
