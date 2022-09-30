package com.paavieira.campsite.reservations.framework.validations;

import com.paavieira.campsite.reservations.framework.controllers.dtos.FindAvailability;
import com.paavieira.campsite.reservations.testing.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PeriodValidatorTest {

    private PeriodValidator validator;

    @BeforeEach
    public void setup() {
        this.validator = new PeriodValidator();
    }

    @Test
    public void isValid_whenPeriodIsValid_thenReturnsTrue() {
        final TestCase testCase = TestCase.whenPeriodIsValid();
        Assertions.assertTrue(testCase.isValid(validator));
    }

    @Test
    public void isValid_whenPeriodIsSameDate_thenReturnsFalse() {
        final TestCase testCase = TestCase.whenPeriodIsSameDate();
        Assertions.assertFalse(testCase.isValid(validator));
    }

    @Test
    public void isValid_whenPeriodIsBackwards_thenReturnsFalse() {
        final TestCase testCase = TestCase.whenPeriodIsBackwards();
        Assertions.assertFalse(testCase.isValid(validator));
    }

    @Test
    public void isValid_whenToIsEmpty_thenReturnsTrue() {
        final TestCase testCase = TestCase.whenToIsEmpty();
        Assertions.assertTrue(testCase.isValid(validator));
    }

    @Test
    public void isValid_whenFindAvailabilityIsNull_thenReturnsTrue() {
        final TestCase testCase = TestCase.whenFindAvailabilityIsNull();
        Assertions.assertTrue(testCase.isValid(validator));
    }

    private static class TestCase {
        private final FindAvailability findAvailability;

        private TestCase(FindAvailability findAvailability) {
            this.findAvailability = findAvailability;
        }

        public static TestCase whenPeriodIsValid() {
            final LocalDate from = RandomUtils.randomDateInTheFuture();
            final LocalDate to = RandomUtils.randomDateAfter(from);
            return new TestCase(new FindAvailability(from, to));
        }

        public static TestCase whenPeriodIsSameDate() {
            final LocalDate from = RandomUtils.randomDateInTheFuture();
            final LocalDate to = from;
            return new TestCase(new FindAvailability(from, to));
        }

        public static TestCase whenPeriodIsBackwards() {
            final LocalDate from = RandomUtils.randomDateInTheFuture();
            final LocalDate to = RandomUtils.randomDateBefore(from);
            return new TestCase(new FindAvailability(from, to));
        }

        public static TestCase whenToIsEmpty() {
            final LocalDate from = RandomUtils.randomDateInTheFuture();
            ;
            return new TestCase(new FindAvailability(from));
        }

        public static TestCase whenFindAvailabilityIsNull() {
            return new TestCase(null);
        }

        public boolean isValid(PeriodValidator validator) {
            return validator.isValid(findAvailability, null);
        }
    }

}
