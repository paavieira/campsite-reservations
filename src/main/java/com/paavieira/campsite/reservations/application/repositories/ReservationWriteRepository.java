package com.paavieira.campsite.reservations.application.repositories;

import com.paavieira.campsite.reservations.domain.Reservation;

public interface ReservationWriteRepository {
    Reservation create(Reservation reservation);

    Reservation modify(Reservation reservation);
}
