package com.paavieira.campsite.reservations.framework.validations;

import com.paavieira.campsite.reservations.framework.controllers.dtos.User;
import com.paavieira.campsite.reservations.testing.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FullNameValidatorTest {

    private FullNameValidator validator;

    @BeforeEach
    public void setup() {
        validator = new FullNameValidator();
    }

    @Test
    public void isValid_whenValidFullName_thenReturnsTrue() {
        final TestCase testCase = TestCase.whenValidFullName();
        Assertions.assertTrue(testCase.isValid(validator));
    }

    @Test
    public void isValid_whenFirstNameIsEmpty_thenReturnsFalse() {
        final TestCase testCase = TestCase.whenFirstNameIsEmpty();
        Assertions.assertFalse(testCase.isValid(validator));
    }

    @Test
    public void isValid_whenFirstNameIsNull_thenReturnsFalse() {
        final TestCase testCase = TestCase.whenFirstNameIsNull();
        Assertions.assertFalse(testCase.isValid(validator));
    }

    @Test
    public void isValid_whenLastNameIsEmpty_thenReturnsFalse() {
        final TestCase testCase = TestCase.whenLastNameIsEmpty();
        Assertions.assertFalse(testCase.isValid(validator));
    }

    @Test
    public void isValid_whenLastNameIsNull_thenReturnsFalse() {
        final TestCase testCase = TestCase.whenLastNameIsNull();
        Assertions.assertFalse(testCase.isValid(validator));
    }

    @Test
    public void isValid_whenUserIsNull_thenReturnsTrue() {
        final TestCase testCase = TestCase.whenUserIsNull();
        Assertions.assertTrue(testCase.isValid(validator));
    }

    private static class TestCase {
        private final User user;

        private TestCase(User user) {
            this.user = user;
        }

        public static TestCase whenValidFullName() {
            return new TestCase(User.fromDomain(RandomUtils.randomUser()));
        }

        public static TestCase whenFirstNameIsEmpty() {
            final User user = new User("", RandomUtils.randomString(), RandomUtils.randomEmail());
            return new TestCase(user);
        }

        public static TestCase whenFirstNameIsNull() {
            final User user = new User(null, RandomUtils.randomString(), RandomUtils.randomEmail());
            return new TestCase(user);
        }

        public static TestCase whenLastNameIsEmpty() {
            final User user = new User(RandomUtils.randomString(), "", RandomUtils.randomEmail());
            return new TestCase(user);
        }

        public static TestCase whenLastNameIsNull() {
            final User user = new User(RandomUtils.randomString(), null, RandomUtils.randomEmail());
            return new TestCase(user);
        }

        public static TestCase whenUserIsNull() {
            return new TestCase(null);
        }

        public boolean isValid(FullNameValidator validator) {
            return validator.isValid(user, null);
        }
    }
}
