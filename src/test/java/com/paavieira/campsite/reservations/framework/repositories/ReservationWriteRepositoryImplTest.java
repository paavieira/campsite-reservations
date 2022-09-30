package com.paavieira.campsite.reservations.framework.repositories;

import com.paavieira.campsite.reservations.application.repositories.ReservationReadRepository;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.exceptions.DateAlreadyReserved;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservationDocument;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservationMongoRepository;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservedDateDocument;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservedDateMongoRepository;
import com.paavieira.campsite.reservations.testing.AssertionsUtils;
import com.paavieira.campsite.reservations.testing.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservationWriteRepositoryImplTest {

    private ReservationWriteRepositoryImpl repository;

    private ReservationReadRepository readRepository;
    private ReservationMongoRepository reservationMongoRepository;
    private ReservedDateMongoRepository reservedDateMongoRepository;

    @BeforeEach
    public void setup() {
        this.readRepository = mock(ReservationReadRepository.class);
        this.reservationMongoRepository = mock(ReservationMongoRepository.class);
        this.reservedDateMongoRepository = mock(ReservedDateMongoRepository.class);
        this.repository = new ReservationWriteRepositoryImpl(readRepository, reservationMongoRepository, reservedDateMongoRepository);
    }

    @Test
    public void create_whenReservationIsValid_thenInsertsReservation_andInsertsReservedDates() {
        final CreateReservationTestCase testCase = CreateReservationTestCase.whenReservationIsValid();
        when(reservationMongoRepository.insert(any(ReservationDocument.class))).thenReturn(testCase.document);
        repository.create(testCase.reservation);
        testCase.assertThatInsertedReservation(reservationMongoRepository);
        testCase.assertThatInsertedReservedDates(reservedDateMongoRepository);
    }

    @Test
    public void create_whenDateIsAlreadyReserved_thenThrowsDateAlreadyReservedException() {
        final CreateReservationTestCase testCase = CreateReservationTestCase.whenDateIsAlreadyReserved(reservedDateMongoRepository);
        final DateAlreadyReserved dateAlreadyReserved = Assertions.assertThrows(DateAlreadyReserved.class,
                                                                                () -> repository.create(testCase.reservation));
        Assertions.assertTrue(dateAlreadyReserved.getMessage().contains("The selected date is already reserved"));
    }

    @Test
    public void modify_thenInsertsReservation_andInsertsReservedDates() {
        final ModifyReservationTestCase testCase = new ModifyReservationTestCase();
        when(readRepository.findActive(testCase.bookingIdentifier)).thenReturn(testCase.staleReservation);
        when(reservationMongoRepository.save(any(ReservationDocument.class))).thenReturn(testCase.document);
        repository.modify(testCase.modifiedReservation);
        testCase.assertThatModifiedReservation(reservationMongoRepository);
        testCase.assertThatRemovedStaleReservedDates(reservedDateMongoRepository);
        testCase.assertThatInsertedModifiedReservedDates(reservedDateMongoRepository);
    }

    private static class CreateReservationTestCase {
        private final Reservation reservation;
        private final ReservationDocument document;

        public CreateReservationTestCase(Reservation reservation) {
            this.reservation = reservation;
            this.document = ReservationDocument.fromDomain(reservation);
        }

        public static CreateReservationTestCase whenReservationIsValid() {
            return new CreateReservationTestCase(RandomUtils.randomValidReservation());
        }

        public static CreateReservationTestCase whenDateIsAlreadyReserved(ReservedDateMongoRepository repository) {
            when(repository.insert(any(ReservedDateDocument.class))).thenThrow(DuplicateKeyException.class);
            return new CreateReservationTestCase(RandomUtils.randomValidReservation());
        }

        public void assertThatInsertedReservation(ReservationMongoRepository repository) {
            final ArgumentCaptor<ReservationDocument> captor = ArgumentCaptor.forClass(ReservationDocument.class);
            verify(repository, times(1)).insert(captor.capture());
            final ReservationDocument actual = captor.getValue();
            AssertionsUtils.assertEquals(document, actual);
        }

        public void assertThatInsertedReservedDates(ReservedDateMongoRepository repository) {
            final List<ReservedDateDocument> expected = reservation.getReservedDates()
                                                                   .stream()
                                                                   .map(ReservedDateDocument::fromDomain)
                                                                   .collect(Collectors.toList());
            final ArgumentCaptor<ReservedDateDocument> captor = ArgumentCaptor.forClass(ReservedDateDocument.class);
            verify(repository, times(expected.size())).insert(captor.capture());
            final List<ReservedDateDocument> actual = captor.getAllValues();
            AssertionsUtils.assertEquals(expected, actual);
        }
    }

    private static class ModifyReservationTestCase {
        private final Reservation staleReservation;
        private final String bookingIdentifier;
        private final Reservation modifiedReservation;
        private final LocalDate modifiedArrivalDate;
        private final LocalDate modifiedDepartureDate;
        private final ReservationDocument document;

        public ModifyReservationTestCase() {
            this.staleReservation = RandomUtils.randomValidReservation();
            this.bookingIdentifier = staleReservation.getBookingIdentifier();
            this.modifiedArrivalDate = RandomUtils.randomDateInTheFuture();
            this.modifiedDepartureDate = RandomUtils.randomDateAfter(modifiedArrivalDate);
            this.modifiedReservation = staleReservation.modify(modifiedArrivalDate, modifiedDepartureDate);
            this.document = ReservationDocument.fromDomain(modifiedReservation);
        }

        public void assertThatModifiedReservation(ReservationMongoRepository repository) {
            final ArgumentCaptor<ReservationDocument> captor = ArgumentCaptor.forClass(ReservationDocument.class);
            verify(repository, times(1)).save(captor.capture());
            final ReservationDocument actual = captor.getValue();
            AssertionsUtils.assertEquals(document, actual);
        }

        public void assertThatRemovedStaleReservedDates(ReservedDateMongoRepository repository) {
            final List<ReservedDateDocument> expected = staleReservation.getReservedDates()
                                                                        .stream()
                                                                        .map(ReservedDateDocument::fromDomain)
                                                                        .collect(Collectors.toList());
            final ArgumentCaptor<List<ReservedDateDocument>> captor = ArgumentCaptor.forClass(List.class);
            verify(repository, times(1)).deleteAll(captor.capture());
            final List<ReservedDateDocument> actual = captor.getValue();
            AssertionsUtils.assertEquals(expected, actual);
        }

        public void assertThatInsertedModifiedReservedDates(ReservedDateMongoRepository reservedDateMongoRepository) {
            final List<ReservedDateDocument> expected = modifiedReservation.getReservedDates()
                                                                           .stream()
                                                                           .map(ReservedDateDocument::fromDomain)
                                                                           .collect(Collectors.toList());
            final ArgumentCaptor<ReservedDateDocument> captor = ArgumentCaptor.forClass(ReservedDateDocument.class);
            verify(reservedDateMongoRepository, times(expected.size())).insert(captor.capture());
            final List<ReservedDateDocument> actual = captor.getAllValues();
            AssertionsUtils.assertEquals(expected, actual);
        }
    }

}
