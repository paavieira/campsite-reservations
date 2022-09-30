package com.paavieira.campsite.reservations.framework.repositories;

import com.paavieira.campsite.reservations.application.repositories.ReservationReadRepository;
import com.paavieira.campsite.reservations.application.repositories.ReservationWriteRepository;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.ReservedDate;
import com.paavieira.campsite.reservations.domain.exceptions.DateAlreadyReserved;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservationMongoRepository;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservedDateDocument;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservedDateMongoRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.paavieira.campsite.reservations.framework.repositories.mongo.ReservationDocument.fromDomain;

@Component
public class ReservationWriteRepositoryImpl implements ReservationWriteRepository {

    private final ReservationReadRepository readRepository;
    private final ReservationMongoRepository reservationMongoRepository;
    private final ReservedDateMongoRepository reservedDateMongoRepository;

    public ReservationWriteRepositoryImpl(ReservationReadRepository readRepository,
                                          ReservationMongoRepository reservationMongoRepository,
                                          ReservedDateMongoRepository reservedDateMongoRepository) {
        this.readRepository = readRepository;
        this.reservationMongoRepository = reservationMongoRepository;
        this.reservedDateMongoRepository = reservedDateMongoRepository;
    }

    @Override
    @Transactional
    public Reservation create(Reservation reservation) {
        createReservedDates(reservation.getReservedDates());
        return this.reservationMongoRepository.insert(fromDomain(reservation)).toDomain();
    }

    @Override
    @Transactional
    public Reservation modify(Reservation updatedReservation) {
        final Reservation staleReservation = readRepository.findActive(updatedReservation.getBookingIdentifier());
        removeReservedDates(staleReservation.getReservedDates());
        createReservedDates(updatedReservation.getReservedDates());
        return this.reservationMongoRepository.save(fromDomain(updatedReservation)).toDomain();
    }

    private void createReservedDates(List<ReservedDate> reservedDates) {
        reservedDates.stream()
                     .map(ReservedDateDocument::fromDomain)
                     .forEach(document -> {
                         try {
                             reservedDateMongoRepository.insert(document);
                         } catch (DuplicateKeyException e) {
                             throw new DateAlreadyReserved(document.toDomain().getDate());
                         }
                     });
    }

    private void removeReservedDates(List<ReservedDate> reservedDates) {
        final List<ReservedDateDocument> documents = reservedDates.stream()
                                                                  .map(ReservedDateDocument::fromDomain)
                                                                  .collect(Collectors.toList());
        reservedDateMongoRepository.deleteAll(documents);
    }

}
