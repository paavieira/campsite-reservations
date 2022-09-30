package com.paavieira.campsite.reservations.domain.services;

import com.paavieira.campsite.reservations.domain.Availability;
import com.paavieira.campsite.reservations.domain.queries.AvailabilityQuery;

public interface FindAvailabilityService {
    Availability findAvailability(AvailabilityQuery query);
}
