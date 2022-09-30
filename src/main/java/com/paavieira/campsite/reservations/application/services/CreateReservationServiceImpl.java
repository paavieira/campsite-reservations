package com.paavieira.campsite.reservations.application.services;

import com.paavieira.campsite.reservations.application.repositories.ReservationWriteRepository;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.ReservationStatus;
import com.paavieira.campsite.reservations.domain.commands.CreateReservationCommand;
import com.paavieira.campsite.reservations.domain.services.CreateReservationService;
import org.springframework.stereotype.Service;

@Service
public class CreateReservationServiceImpl implements CreateReservationService {

    private final ReservationWriteRepository reservationWriteRepository;

    public CreateReservationServiceImpl(ReservationWriteRepository reservationWriteRepository) {
        this.reservationWriteRepository = reservationWriteRepository;
    }

    @Override
    public Reservation createReservation(CreateReservationCommand command) {
        final Reservation reservation = Reservation.create(
                command.getBookingIdentifier(),
                command.getCreatedDate(),
                command.getArrivalDate(),
                command.getDepartureDate(),
                ReservationStatus.ACTIVE,
                command.getUser()
        );
        return this.reservationWriteRepository.create(reservation);
    }

}
