package com.paavieira.campsite.reservations.application.services;

import com.paavieira.campsite.reservations.application.repositories.ReservationReadRepository;
import com.paavieira.campsite.reservations.application.repositories.ReservationWriteRepository;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.commands.CancelReservationCommand;
import com.paavieira.campsite.reservations.domain.services.CancelReservationService;
import org.springframework.stereotype.Service;

@Service
public class CancelReservationServiceImpl implements CancelReservationService {
    private final ReservationReadRepository reservationReadRepository;
    private final ReservationWriteRepository reservationWriteRepository;

    public CancelReservationServiceImpl(ReservationReadRepository reservationReadRepository,
                                        ReservationWriteRepository reservationWriteRepository) {
        this.reservationReadRepository = reservationReadRepository;
        this.reservationWriteRepository = reservationWriteRepository;
    }

    @Override
    public Reservation cancelReservation(CancelReservationCommand command) {
        final Reservation activeReservation = reservationReadRepository.findActive(command.getBookingIdentifier());
        final Reservation cancelledReservation = activeReservation.cancel();
        return reservationWriteRepository.modify(cancelledReservation);
    }
}
