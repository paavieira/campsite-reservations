package com.paavieira.campsite.reservations.domain.services;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.commands.CancelReservationCommand;

public interface CancelReservationService {
    Reservation cancelReservation(CancelReservationCommand command);
}
