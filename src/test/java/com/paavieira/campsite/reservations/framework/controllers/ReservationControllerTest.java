package com.paavieira.campsite.reservations.framework.controllers;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.commands.CancelReservationCommand;
import com.paavieira.campsite.reservations.domain.commands.CreateReservationCommand;
import com.paavieira.campsite.reservations.domain.commands.ModifyReservationCommand;
import com.paavieira.campsite.reservations.domain.services.CancelReservationService;
import com.paavieira.campsite.reservations.domain.services.CreateReservationService;
import com.paavieira.campsite.reservations.domain.services.ModifyReservationService;
import com.paavieira.campsite.reservations.framework.controllers.dtos.CreateReservation;
import com.paavieira.campsite.reservations.framework.controllers.dtos.ModifyReservation;
import com.paavieira.campsite.reservations.framework.controllers.dtos.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomDateAfter;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomDateInTheFuture;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomValidReservation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservationControllerTest {

    private ReservationController controller;
    private CreateReservationService createReservationService;
    private ModifyReservationService modifyReservationService;
    private CancelReservationService cancelReservationService;

    @BeforeEach
    public void setup() {
        createReservationService = mock(CreateReservationService.class);
        modifyReservationService = mock(ModifyReservationService.class);
        cancelReservationService = mock(CancelReservationService.class);
        controller = new ReservationController(createReservationService, modifyReservationService, cancelReservationService);
    }

    @Test
    public void create_thenCreatesWithExpectedParameters() {
        final CreateReservationTestCase testCase = new CreateReservationTestCase();
        when(createReservationService.createReservation(any())).thenReturn(testCase.reservation);
        controller.create(testCase.createReservation);
        final ArgumentCaptor<CreateReservationCommand> captor = ArgumentCaptor.forClass(CreateReservationCommand.class);
        verify(createReservationService, times(1)).createReservation(captor.capture());
        final CreateReservationCommand command = captor.getValue();
        assertNotNull(command.getBookingIdentifier());
        assertEquals(LocalDate.now(), command.getCreatedDate());
        assertEquals(testCase.createReservation.getArrivalDate(), command.getArrivalDate());
        assertEquals(testCase.createReservation.getDepartureDate(), command.getDepartureDate());
        assertEquals(testCase.createReservation.getUser().getFirstName(), command.getUser().getFirstName());
        assertEquals(testCase.createReservation.getUser().getLastName(), command.getUser().getLastName());
        assertEquals(testCase.createReservation.getUser().getEmail(), command.getUser().getEmail());
    }

    @Test
    public void modify_thenModifiesWithExpectedParameters() {
        final ModifyReservationTestCase testCase = new ModifyReservationTestCase();
        when(modifyReservationService.modifyReservation(any())).thenReturn(testCase.reservation);
        controller.modify(testCase.bookingIdentifier, testCase.modifyReservation);
        final ArgumentCaptor<ModifyReservationCommand> captor = ArgumentCaptor.forClass(ModifyReservationCommand.class);
        verify(modifyReservationService, times(1)).modifyReservation(captor.capture());
        final ModifyReservationCommand command = captor.getValue();
        assertEquals(testCase.bookingIdentifier, command.getBookingIdentifier());
        assertEquals(testCase.modifiedArrivalDate, command.getArrivalDate());
        assertEquals(testCase.modifiedDepartureDate, command.getDepartureDate());
    }

    @Test
    public void cancel_thenCancelsWithExpectedParameters() {
        final CancelReservationTestCase testCase = new CancelReservationTestCase();
        when(cancelReservationService.cancelReservation(any())).thenReturn(testCase.reservation);
        controller.cancel(testCase.bookingIdentifier);
        final ArgumentCaptor<CancelReservationCommand> captor = ArgumentCaptor.forClass(CancelReservationCommand.class);
        verify(cancelReservationService, times(1)).cancelReservation(captor.capture());
        final CancelReservationCommand command = captor.getValue();
        assertEquals(testCase.bookingIdentifier, command.getBookingIdentifier());
    }

    private static class CreateReservationTestCase {
        private final Reservation reservation;
        private final CreateReservation createReservation;

        public CreateReservationTestCase() {
            this.reservation = randomValidReservation();
            this.createReservation = new CreateReservation(User.fromDomain(reservation.getUser()), reservation.getArrivalDate(),
                                                           reservation.getDepartureDate());
        }
    }

    private static class ModifyReservationTestCase {
        private final String bookingIdentifier;
        private final Reservation reservation;
        private final ModifyReservation modifyReservation;
        private final LocalDate modifiedArrivalDate;
        private final LocalDate modifiedDepartureDate;

        public ModifyReservationTestCase() {
            this.reservation = randomValidReservation();
            this.bookingIdentifier = reservation.getBookingIdentifier();
            this.modifiedArrivalDate = randomDateInTheFuture();
            this.modifiedDepartureDate = randomDateAfter(modifiedArrivalDate);
            this.modifyReservation = new ModifyReservation(modifiedArrivalDate, modifiedDepartureDate);
        }
    }

    private static class CancelReservationTestCase {
        private final String bookingIdentifier;
        private final Reservation reservation;

        public CancelReservationTestCase() {
            this.reservation = randomValidReservation();
            this.bookingIdentifier = reservation.getBookingIdentifier();
        }
    }
}
