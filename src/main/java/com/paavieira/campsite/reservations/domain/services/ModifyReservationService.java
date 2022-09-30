package com.paavieira.campsite.reservations.domain.services;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.commands.ModifyReservationCommand;

public interface ModifyReservationService {
    Reservation modifyReservation(ModifyReservationCommand command);
}
