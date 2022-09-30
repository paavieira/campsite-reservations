package com.paavieira.campsite.reservations.framework.controllers;

import com.paavieira.campsite.reservations.domain.queries.AvailabilityQuery;
import com.paavieira.campsite.reservations.domain.services.FindAvailabilityService;
import com.paavieira.campsite.reservations.framework.controllers.dtos.Availability;
import com.paavieira.campsite.reservations.framework.controllers.dtos.FindAvailability;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/availabilities")
public class AvailabilityController {

    private final FindAvailabilityService findAvailabilityService;

    public AvailabilityController(FindAvailabilityService findAvailabilityService) {
        this.findAvailabilityService = findAvailabilityService;
    }

    @PostMapping
    @ResponseBody
    public Availability query(@Valid @RequestBody FindAvailability findAvailability) {
        final AvailabilityQuery query = new AvailabilityQuery(findAvailability.getFrom(), findAvailability.getToOrDefault());
        return Availability.fromDomain(findAvailabilityService.findAvailability(query));
    }

}