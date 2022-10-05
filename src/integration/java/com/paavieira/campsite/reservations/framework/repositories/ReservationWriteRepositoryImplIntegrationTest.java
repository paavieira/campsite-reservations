package com.paavieira.campsite.reservations.framework.repositories;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.exceptions.DateAlreadyReserved;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservedDateDocument;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservedDateMongoRepository;
import com.paavieira.campsite.reservations.testing.AssertionsUtils;
import com.paavieira.campsite.reservations.testing.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationWriteRepositoryImplIntegrationTest extends MongoDBIntegrationTest {

    private ReservationReadRepositoryImpl readRepository;
    private ReservationWriteRepositoryImpl writeRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void setup() {
        readRepository = new ReservationReadRepositoryImpl(mongoTemplate);
        writeRepository = new ReservationWriteRepositoryImpl(readRepository, reservationMongoRepository, reservedDateMongoRepository);
    }

    @Test
    public void create_whenValidReservation_thenCreatesReservation() {
        final CreateReservationTestCase testCase = new CreateReservationTestCase();
        final Reservation actual = writeRepository.create(testCase.reservation);
        AssertionsUtils.assertEquals(testCase.reservation, actual);
    }

    @Test
    public void create_whenDateAlreadyReserved_thenThrowsDateAlreadyReserved() {
        final CreateReservationTestCase testCase = new CreateReservationTestCase();
        writeRepository.create(testCase.reservation);
        Assertions.assertThrows(DateAlreadyReserved.class, () -> writeRepository.create(testCase.conflictingReservation));
    }

    @Test
    public void modify_whenValidModifiedReservation_thenModifiesReservation() {
        final ModifyReservationTestCase testCase = new ModifyReservationTestCase();
        writeRepository.create(testCase.reservation);
        final Reservation actual = writeRepository.modify(testCase.modifiedReservation);
        AssertionsUtils.assertEquals(testCase.modifiedReservation, actual);
    }

    @Test
    public void modify_whenDateAlreadyReserved_thenThrowsDateAlreadyReserved() {
        final ModifyReservationTestCase testCase = new ModifyReservationTestCase();
        writeRepository.create(testCase.reservation);
        writeRepository.create(testCase.conflictingReservation);
        Assertions.assertThrows(DateAlreadyReserved.class, () -> writeRepository.modify(testCase.modifiedReservation));
    }

    @Test
    public void modify_whenThereAreConflictingModifications_thenThrowsOptimisticLockingFailureException() {
        final ModifyReservationTestCase testCase = new ModifyReservationTestCase();
        writeRepository.create(testCase.reservation);
        writeRepository.modify(testCase.modifiedReservation);
        Assertions.assertThrows(OptimisticLockingFailureException.class, () -> writeRepository.modify(testCase.modifiedReservation));
    }

    @Test
    public void modify_whenCancelling_thenRemovesReservedDates() {
        final ModifyReservationTestCase testCase = new ModifyReservationTestCase();
        writeRepository.create(testCase.reservation);
        writeRepository.modify(testCase.reservation.cancel());
        testCase.assertThatRemovedAllReservedDates(reservedDateMongoRepository);
    }

    private static class CreateReservationTestCase {
        private final Reservation reservation;
        private final Reservation conflictingReservation;

        public CreateReservationTestCase() {
            this.reservation = RandomUtils.randomValidReservation();
            this.conflictingReservation = RandomUtils.randomValidReservation()
                                                     .modify(reservation.getArrivalDate(), reservation.getDepartureDate());
        }
    }

    private static class ModifyReservationTestCase {
        private final Reservation reservation;
        private final Reservation modifiedReservation;
        private final Reservation conflictingReservation;

        public ModifyReservationTestCase() {
            this.reservation = RandomUtils.randomValidReservation();
            this.modifiedReservation = reservation.modify(reservation.getArrivalDate().plusDays(7),
                                                          reservation.getDepartureDate().plusDays(7));
            this.conflictingReservation = RandomUtils.randomValidReservation()
                                                     .modify(modifiedReservation.getArrivalDate(), modifiedReservation.getDepartureDate());
        }

        public void assertThatRemovedAllReservedDates(ReservedDateMongoRepository repository) {
            final List<String> ids = reservation.getReservedDates()
                                                .stream()
                                                .map(ReservedDateDocument::fromDomain)
                                                .map(ReservedDateDocument::getBookingIdentifier)
                                                .collect(Collectors.toList());
            Assertions.assertFalse(repository.findAllById(ids).iterator().hasNext());
        }

    }
}
