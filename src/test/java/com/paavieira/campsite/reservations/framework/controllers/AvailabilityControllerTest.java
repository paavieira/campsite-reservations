package com.paavieira.campsite.reservations.framework.controllers;

import com.paavieira.campsite.reservations.domain.Availability;
import com.paavieira.campsite.reservations.domain.queries.AvailabilityQuery;
import com.paavieira.campsite.reservations.domain.services.FindAvailabilityService;
import com.paavieira.campsite.reservations.framework.controllers.dtos.FindAvailability;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomAvailability;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AvailabilityControllerTest {

    private AvailabilityController controller;
    private FindAvailabilityService service;

    @BeforeEach
    public void setup() {
        service = mock(FindAvailabilityService.class);
        controller = new AvailabilityController(service);
    }

    @Test
    public void query_thenQueriesWithExpectedParameters() {
        final TestCase testCase = new TestCase();
        when(service.findAvailability(any())).thenReturn(testCase.availability);
        controller.query(testCase.findAvailability);
        final ArgumentCaptor<AvailabilityQuery> captor = ArgumentCaptor.forClass(AvailabilityQuery.class);
        verify(service, times(1)).findAvailability(captor.capture());
        final AvailabilityQuery query = captor.getValue();
        Assertions.assertEquals(testCase.findAvailability.getFrom(), query.getFrom());
        Assertions.assertEquals(testCase.findAvailability.getTo(), query.getTo());
    }

    private static class TestCase {
        private final Availability availability;
        private final FindAvailability findAvailability;

        public TestCase() {
            this.availability = randomAvailability();
            this.findAvailability = new FindAvailability(availability.getFrom(), availability.getTo());
        }
    }
}
