package com.paavieira.campsite.reservations.application.services;

import com.paavieira.campsite.reservations.application.repositories.ReservationReadRepository;
import com.paavieira.campsite.reservations.application.repositories.ReservationWriteRepository;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.commands.ModifyReservationCommand;
import com.paavieira.campsite.reservations.domain.services.ModifyReservationService;
import org.springframework.stereotype.Service;

@Service
public class ModifyReservationServiceImpl implements ModifyReservationService {

    private final ReservationReadRepository readRepository;
    private final ReservationWriteRepository writeRepository;

    public ModifyReservationServiceImpl(ReservationReadRepository readRepository, ReservationWriteRepository writeRepository) {
        this.readRepository = readRepository;
        this.writeRepository = writeRepository;
    }

    @Override
    public Reservation modifyReservation(ModifyReservationCommand command) {
        final Reservation staleReservation = readRepository.findActive(command.getBookingIdentifier());
        final Reservation updatedReservation = staleReservation.modify(command.getArrivalDate(), command.getDepartureDate());
        return this.writeRepository.modify(updatedReservation);
    }

}
