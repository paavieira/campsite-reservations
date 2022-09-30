package com.paavieira.campsite.reservations.domain.services;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.commands.CreateReservationCommand;

public interface CreateReservationService {
    Reservation createReservation(CreateReservationCommand command);
}
