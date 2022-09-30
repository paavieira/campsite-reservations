package com.paavieira.campsite.reservations.application.services;

import com.paavieira.campsite.reservations.application.repositories.ReservationReadRepository;
import com.paavieira.campsite.reservations.domain.Availability;
import com.paavieira.campsite.reservations.domain.queries.AvailabilityQuery;
import com.paavieira.campsite.reservations.domain.services.FindAvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FindAvailabilityServiceImpl implements FindAvailabilityService {

    private final ReservationReadRepository repository;

    public FindAvailabilityServiceImpl(ReservationReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Availability findAvailability(AvailabilityQuery query) {
        final LocalDate from = query.getFrom();
        final LocalDate to = query.getTo();
        return this.repository.listActive(from, to).isEmpty() ? Availability.available(from, to) : Availability.unavailable(from, to);
    }
}
