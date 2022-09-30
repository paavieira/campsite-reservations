package com.paavieira.campsite.reservations.application.services;

import com.paavieira.campsite.reservations.application.repositories.ReservationReadRepository;
import com.paavieira.campsite.reservations.application.repositories.ReservationWriteRepository;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.commands.ModifyReservationCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomDateInTheFuture;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomInt;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomValidReservation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ModifyReservationServiceImplTest {

    private ModifyReservationServiceImpl service;

    private ReservationReadRepository readRepository;
    private ReservationWriteRepository writeRepository;


    @BeforeEach
    public void setup() {
        readRepository = mock(ReservationReadRepository.class);
        writeRepository = mock(ReservationWriteRepository.class);
        service = new ModifyReservationServiceImpl(readRepository, writeRepository);
    }

    @Test
    public void modifyReservation_thenModifiesReservation() {
        final TestCase testCase = new TestCase();
        when(readRepository.findActive(testCase.bookingIdentifier)).thenReturn(testCase.reservation);
        service.modifyReservation(testCase.command);
        final ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(writeRepository, times(1)).modify(captor.capture());
        final Reservation modifiedReservation = captor.getValue();
        Assertions.assertEquals(testCase.bookingIdentifier, modifiedReservation.getBookingIdentifier());
        Assertions.assertEquals(testCase.modifiedArrivalDate, modifiedReservation.getArrivalDate());
        Assertions.assertEquals(testCase.modifiedDepartureDate, modifiedReservation.getDepartureDate());
    }

    private static class TestCase {
        private final String bookingIdentifier;
        private final Reservation reservation;
        private final LocalDate modifiedArrivalDate;
        private final LocalDate modifiedDepartureDate;
        private final ModifyReservationCommand command;

        public TestCase() {
            this.reservation = randomValidReservation();
            this.bookingIdentifier = reservation.getBookingIdentifier();
            this.modifiedArrivalDate = randomDateInTheFuture();
            this.modifiedDepartureDate = modifiedArrivalDate.plusDays(randomInt(1, 3));
            this.command = new ModifyReservationCommand(reservation.getBookingIdentifier(), modifiedArrivalDate, modifiedDepartureDate);
        }
    }
}
