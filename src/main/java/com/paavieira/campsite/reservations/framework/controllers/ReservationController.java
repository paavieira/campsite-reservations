package com.paavieira.campsite.reservations.framework.controllers;

import com.paavieira.campsite.reservations.domain.commands.CancelReservationCommand;
import com.paavieira.campsite.reservations.domain.commands.CreateReservationCommand;
import com.paavieira.campsite.reservations.domain.commands.ModifyReservationCommand;
import com.paavieira.campsite.reservations.domain.services.CancelReservationService;
import com.paavieira.campsite.reservations.domain.services.CreateReservationService;
import com.paavieira.campsite.reservations.domain.services.ModifyReservationService;
import com.paavieira.campsite.reservations.framework.controllers.dtos.CreateReservation;
import com.paavieira.campsite.reservations.framework.controllers.dtos.ModifyReservation;
import com.paavieira.campsite.reservations.framework.controllers.dtos.Reservation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final CreateReservationService createReservationService;
    private final ModifyReservationService modifyReservationService;
    private final CancelReservationService cancelReservationService;

    public ReservationController(CreateReservationService createReservationService,
                                 ModifyReservationService modifyReservationService,
                                 CancelReservationService cancelReservationService) {
        this.createReservationService = createReservationService;
        this.modifyReservationService = modifyReservationService;
        this.cancelReservationService = cancelReservationService;
    }

    @PostMapping
    @ResponseBody
    public Reservation create(@RequestBody @Valid @NotEmpty CreateReservation createReservation) {
        final CreateReservationCommand command = new CreateReservationCommand(
                LocalDate.now(),
                UUID.randomUUID().toString(),
                createReservation.getUser().toDomain(),
                createReservation.getArrivalDate(),
                createReservation.getDepartureDate()
        );
        return Reservation.fromDomain(createReservationService.createReservation(command));
    }

    @PostMapping("/{id}")
    @ResponseBody
    public Reservation modify(@PathVariable("id") final String bookingIdentifier,
                              @RequestBody @Valid @NotEmpty ModifyReservation modifyReservation) {
        final ModifyReservationCommand command = new ModifyReservationCommand(
                bookingIdentifier,
                modifyReservation.getArrivalDate(),
                modifyReservation.getDepartureDate()
        );
        return Reservation.fromDomain(modifyReservationService.modifyReservation(command));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Reservation cancel(@PathVariable("id") final String bookingIdentifier) {
        final CancelReservationCommand command = new CancelReservationCommand(bookingIdentifier);
        return Reservation.fromDomain(cancelReservationService.cancelReservation(command));
    }

}