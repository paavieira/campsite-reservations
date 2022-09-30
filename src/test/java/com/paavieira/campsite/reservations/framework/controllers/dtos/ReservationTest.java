package com.paavieira.campsite.reservations.framework.controllers.dtos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomValidReservation;

public class ReservationTest {

    @Test
    public void fromDomain_thenReturnsExpectedDto() {
        final TestCase testCase = new TestCase();
        final Reservation dto = Reservation.fromDomain(testCase.domain);
        Assertions.assertEquals(testCase.domain.getBookingIdentifier(), dto.getBookingIdentifier());
        Assertions.assertEquals(testCase.domain.getCreationDate(), dto.getCreationDate());
        Assertions.assertEquals(testCase.domain.getArrivalDate(), dto.getArrivalDate());
        Assertions.assertEquals(testCase.domain.getDepartureDate(), dto.getDepartureDate());
        Assertions.assertEquals(testCase.domain.getStatus(), dto.getStatus());
        Assertions.assertEquals(testCase.domain.getUser().getFirstName(), dto.getUser().getFirstName());
        Assertions.assertEquals(testCase.domain.getUser().getLastName(), dto.getUser().getLastName());
        Assertions.assertEquals(testCase.domain.getUser().getEmail(), dto.getUser().getEmail());
    }

    private static class TestCase {
        private final com.paavieira.campsite.reservations.domain.Reservation domain;

        public TestCase() {
            this.domain = randomValidReservation();
        }
    }
}
