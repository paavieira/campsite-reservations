package com.paavieira.campsite.reservations.domain;

import com.paavieira.campsite.reservations.domain.exceptions.InvalidPeriod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomDateInTheFuture;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomInt;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AvailabilityTest {

    @Test
    public void isAvailable_whenAvailable_thenReturnsTrue() {
        final TestCase testCase = TestCase.whenAvailable();
        Assertions.assertTrue(testCase.availability.isAvailable());
    }

    @Test
    public void isAvailable_whenUnavailable_thenReturnsTrue() {
        final TestCase testCase = TestCase.whenUnavailable();
        Assertions.assertFalse(testCase.availability.isAvailable());
    }

    @Test
    public void isAvailable_whenInvalidPeriod_thenThrowsInvalidPeriodException() {
        final InvalidPeriod invalidPeriod = assertThrows(InvalidPeriod.class, () -> TestCase.whenInvalidDuration());
        Assertions.assertTrue(invalidPeriod.getMessage().contains("The period is invalid"));
    }

    private static class TestCase {
        private final Availability availability;

        public TestCase(Availability availability) {
            this.availability = availability;
        }

        public static TestCase whenAvailable() {
            final LocalDate from = randomDateInTheFuture();
            final LocalDate to = from.plusDays(randomInt(1, 10));
            return new TestCase(Availability.available(from, to));
        }

        public static TestCase whenUnavailable() {
            final LocalDate from = randomDateInTheFuture();
            final LocalDate to = from.plusDays(randomInt(1, 10));
            return new TestCase(Availability.unavailable(from, to));
        }

        public static TestCase whenInvalidDuration() {
            final LocalDate from = randomDateInTheFuture();
            final LocalDate to = from.minusDays(randomInt(1, 10));
            return new TestCase(Availability.available(from, to));
        }

    }
}
