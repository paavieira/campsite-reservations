package com.paavieira.campsite.reservations.framework.controllers.dtos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomAvailability;

public class AvailabilityTest {

    @Test
    public void fromDomain_thenReturnsExpectedDto() {
        final TestCase testCase = new TestCase();
        final Availability dto = Availability.fromDomain(testCase.domain);
        Assertions.assertEquals(testCase.domain.getFrom(), dto.getFrom());
        Assertions.assertEquals(testCase.domain.getTo(), dto.getTo());
        Assertions.assertEquals(testCase.domain.isAvailable(), dto.isAvailable());
    }

    private static class TestCase {
        private final com.paavieira.campsite.reservations.domain.Availability domain;

        public TestCase() {
            this.domain = randomAvailability();
        }
    }
}
