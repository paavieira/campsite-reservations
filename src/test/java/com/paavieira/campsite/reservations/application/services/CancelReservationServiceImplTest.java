package com.paavieira.campsite.reservations.application.services;

import com.paavieira.campsite.reservations.application.repositories.ReservationReadRepository;
import com.paavieira.campsite.reservations.application.repositories.ReservationWriteRepository;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.commands.CancelReservationCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomValidReservation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CancelReservationServiceImplTest {

    private CancelReservationServiceImpl service;
    private ReservationReadRepository reservationReadRepository;
    private ReservationWriteRepository reservationWriteRepository;

    @BeforeEach
    public void setup() {
        reservationReadRepository = mock(ReservationReadRepository.class);
        reservationWriteRepository = mock(ReservationWriteRepository.class);
        service = new CancelReservationServiceImpl(reservationReadRepository, reservationWriteRepository);
    }

    @Test
    public void cancel_thenCancelsReservation() {
        final TestCase testCase = new TestCase();
        when(reservationReadRepository.findActive(testCase.bookingIdentifier)).thenReturn(testCase.reservation);
        service.cancelReservation(testCase.command);
        final ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationWriteRepository, times(1)).modify(captor.capture());
        final Reservation cancelledReservation = captor.getValue();
        Assertions.assertEquals(testCase.bookingIdentifier, cancelledReservation.getBookingIdentifier());
        Assertions.assertTrue(cancelledReservation.isCancelled());
    }

    private static class TestCase {
        private final String bookingIdentifier;
        private final Reservation reservation;
        private final CancelReservationCommand command;

        public TestCase() {
            this.reservation = randomValidReservation();
            this.bookingIdentifier = reservation.getBookingIdentifier();
            this.command = new CancelReservationCommand(bookingIdentifier);
        }
    }
}
