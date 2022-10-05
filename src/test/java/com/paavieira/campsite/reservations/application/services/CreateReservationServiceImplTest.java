package com.paavieira.campsite.reservations.application.services;

import com.paavieira.campsite.reservations.application.repositories.ReservationWriteRepository;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.ReservationStatus;
import com.paavieira.campsite.reservations.domain.commands.CreateReservationCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomValidReservation;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CreateReservationServiceImplTest {

    private CreateReservationServiceImpl service;

    private ReservationWriteRepository writeRepository;


    @BeforeEach
    public void setup() {
        writeRepository = mock(ReservationWriteRepository.class);
        service = new CreateReservationServiceImpl(writeRepository);
    }

    @Test
    public void createReservation_thenCreatesReservation() {
        final TestCase testCase = new TestCase();
        try (MockedStatic<Reservation> mockedStatic = mockStatic(Reservation.class)) {
            mockedStatic.when(() -> Reservation.create(
                    eq(testCase.command.getBookingIdentifier()),
                    eq(testCase.command.getCreatedDate()),
                    eq(testCase.command.getArrivalDate()),
                    eq(testCase.command.getDepartureDate()),
                    eq(ReservationStatus.ACTIVE),
                    eq(testCase.command.getUser()),
                    eq(Reservation.INITIAL_VERSION)
            )).thenReturn(testCase.reservation);
            service.createReservation(testCase.command);
            verify(writeRepository, times(1)).create(testCase.reservation);
        }
    }

    private static class TestCase {
        private final Reservation reservation;
        private final CreateReservationCommand command;

        public TestCase() {
            this.reservation = randomValidReservation();
            this.command = new CreateReservationCommand(reservation.getCreationDate(),
                                                        reservation.getBookingIdentifier(),
                                                        reservation.getUser(),
                                                        reservation.getArrivalDate(),
                                                        reservation.getDepartureDate());
        }
    }
}
