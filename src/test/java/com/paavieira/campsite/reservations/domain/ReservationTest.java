package com.paavieira.campsite.reservations.domain;

import com.paavieira.campsite.reservations.domain.exceptions.InvalidArrivalDate;
import com.paavieira.campsite.reservations.domain.exceptions.InvalidDepartureDate;
import com.paavieira.campsite.reservations.domain.exceptions.InvalidDuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomId;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomInt;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomInvalidDuration;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomUser;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomValidDuration;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomVersion;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservationTest {

    private int startOffset;

    @BeforeEach
    public void setup() {
        startOffset = randomInt(2, 10);
    }

    @Test
    public void create_whenPeriodIsBackwards_thenThrowsInvalidDepartureDateException() {
        final TestCase testCase = TestCase.whenPeriodIsBackwards(startOffset);
        final InvalidDepartureDate invalidDuration = assertThrows(InvalidDepartureDate.class, () -> testCase.createReservation());
        assertTrue(invalidDuration.getMessage().contains("The departure date must be after the arrival date"));
    }

    @Test
    public void create_whenDepartureDateSameAsArrivalDate_thenThrowsInvalidDepartureDateException() {
        final TestCase testCase = TestCase.whenDepartureDateSameAsArrivalDate();
        final InvalidDepartureDate invalidDuration = assertThrows(InvalidDepartureDate.class, () -> testCase.createReservation());
        assertTrue(invalidDuration.getMessage().contains("The departure date must be after the arrival date"));
    }

    @Test
    public void create_whenDurationIsTooLong_thenThrowsInvalidDurationException() {
        final TestCase testCase = TestCase.whenDurationIsTooLong(startOffset);
        final InvalidDuration invalidDuration = assertThrows(InvalidDuration.class, () -> testCase.createReservation());
        assertTrue(invalidDuration.getMessage().contains("The campsite can be reserved for max 3 days"));
    }

    @Test
    public void create_whenReservationStartsTooSoon_thenThrowsInvalidArrivalDate() {
        final TestCase testCase = TestCase.whenReservationStartsTooSoon();
        final InvalidArrivalDate invalidArrivalDate = assertThrows(InvalidArrivalDate.class, () -> testCase.createReservation());
        assertTrue(invalidArrivalDate.getMessage()
                                     .contains(
                                             "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance"));
    }

    @Test
    public void create_whenReservationStartsTooDistant_thenThrowsInvalidArrivalDate() {
        final TestCase testCase = TestCase.whenReservationStartsTooDistant(startOffset);
        final InvalidArrivalDate invalidArrivalDate = assertThrows(InvalidArrivalDate.class, () -> testCase.createReservation());
        assertTrue(invalidArrivalDate.getMessage()
                                     .contains(
                                             "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance"));
    }

    @Test
    public void create_whenReservationStartsInOneMonth_thenCreatesReservationAsExpected() {
        final TestCase testCase = TestCase.whenReservationStartsInOneMonth();
        final Reservation reservation = testCase.createReservation();
        testCase.assertThatReservationWasCreatedAsExpected(reservation);
    }

    @Test
    public void create_whenReservationStartsTomorrow_thenCreatesReservationAsExpected() {
        final TestCase testCase = TestCase.whenReservationStartsTomorrow();
        final Reservation reservation = testCase.createReservation();
        testCase.assertThatReservationWasCreatedAsExpected(reservation);
    }

    @Test
    public void modify_whenPeriodIsBackwards_thenThrowsInvalidDepartureDateException() {
        final TestCase createTestCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = createTestCase.createReservation();
        final TestCase modifyTestCase = TestCase.whenPeriodIsBackwards(startOffset);
        final InvalidDepartureDate invalidDepartureDate = assertThrows(InvalidDepartureDate.class,
                                                                       () -> modifyTestCase.modifyReservation(reservation));
        assertTrue(invalidDepartureDate.getMessage().contains("The departure date must be after the arrival date"));
    }

    @Test
    public void modify_whenDepartureDateSameAsArrivalDate_thenThrowsInvalidDepartureDateException() {
        final TestCase createTestCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = createTestCase.createReservation();
        final TestCase modifyTestCase = TestCase.whenDepartureDateSameAsArrivalDate();
        final InvalidDepartureDate invalidDuration = assertThrows(InvalidDepartureDate.class,
                                                                  () -> modifyTestCase.modifyReservation(reservation));
        assertTrue(invalidDuration.getMessage().contains("The departure date must be after the arrival date"));
    }

    @Test
    public void modify_whenDurationIsTooLong_thenThrowsInvalidDurationException() {
        final TestCase createTestCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = createTestCase.createReservation();
        final TestCase modifyTestCase = TestCase.whenDurationIsTooLong(startOffset);
        final InvalidDuration invalidDuration = assertThrows(InvalidDuration.class, () -> modifyTestCase.modifyReservation(reservation));
        assertTrue(invalidDuration.getMessage().contains("The campsite can be reserved for max 3 days"));
    }

    @Test
    public void modify_whenReservationStartsTooSoon_thenThrowsInvalidArrivalDate() {
        final TestCase createTestCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = createTestCase.createReservation();
        final TestCase modifyTestCase = TestCase.whenReservationStartsTooSoon();
        final InvalidArrivalDate invalidArrivalDate = assertThrows(InvalidArrivalDate.class,
                                                                   () -> modifyTestCase.modifyReservation(reservation));
        assertTrue(invalidArrivalDate.getMessage()
                                     .contains(
                                             "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance"));
    }

    @Test
    public void modify_whenReservationStartsTooDistant_thenThrowsInvalidArrivalDate() {
        final TestCase createTestCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = createTestCase.createReservation();
        final TestCase modifyTestCase = TestCase.whenReservationStartsTooDistant(startOffset);
        final InvalidArrivalDate invalidArrivalDate = assertThrows(InvalidArrivalDate.class,
                                                                   () -> modifyTestCase.modifyReservation(reservation));
        assertTrue(invalidArrivalDate.getMessage()
                                     .contains(
                                             "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance"));
    }

    @Test
    public void modify_whenReservationStartsInOneMonth_thenModifiesReservationAsExpected() {
        final TestCase createTestCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = createTestCase.createReservation();
        final TestCase modifyTestCase = TestCase.whenReservationStartsInOneMonth();
        final Reservation updatedReservation = modifyTestCase.modifyReservation(reservation);
        modifyTestCase.assertThatReservationWasUpdatedAsExpected(reservation, updatedReservation);
    }

    @Test
    public void modify_whenReservationStartsTomorrow_thenModifiesReservationAsExpected() {
        final TestCase createTestCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = createTestCase.createReservation();
        final TestCase modifyTestCase = TestCase.whenReservationStartsTomorrow();
        final Reservation updatedReservation = modifyTestCase.modifyReservation(reservation);
        modifyTestCase.assertThatReservationWasUpdatedAsExpected(reservation, updatedReservation);
    }

    @Test
    public void cancel_thenCancelsReservationAsExpected() {
        final TestCase testCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = testCase.createReservation();
        final Reservation cancelledReservation = testCase.cancelReservation(reservation);
        testCase.assertThatReservationWasCancelledAsExpected(cancelledReservation);
    }

    @Test
    public void getReservedDates_whenValidDuration_thenReturnsExpectedDates() {
        final TestCase testCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = testCase.createReservation();
        final List<ReservedDate> reservedDates = reservation.getReservedDates();
        testCase.assertContainsReservedDates(reservedDates);
    }

    @Test
    public void getReservedDates_whenReservationIsCancelled_thenReturnsEmpty() {
        final TestCase testCase = TestCase.whenValidDuration(startOffset);
        final Reservation reservation = testCase.createReservation().cancel();
        final List<ReservedDate> reservedDates = reservation.getReservedDates();
        assertTrue(reservedDates.isEmpty());
    }

    private static class TestCase {

        private final LocalDate arrivalDate;
        private final LocalDate departureDate;
        private final String bookingIdentifier;
        private final LocalDate creationDate;
        private final ReservationStatus status;
        private final User user;
        private final Long version;

        public TestCase(LocalDate arrivalDate, LocalDate departureDate) {
            this.arrivalDate = arrivalDate;
            this.departureDate = departureDate;
            this.bookingIdentifier = randomId();
            this.creationDate = LocalDate.now();
            this.status = ReservationStatus.ACTIVE;
            this.user = randomUser();
            this.version = randomVersion();
        }

        public Reservation createReservation() {
            return Reservation.create(bookingIdentifier, creationDate, arrivalDate, departureDate, status, user, version);
        }

        public Reservation modifyReservation(Reservation reservation) {
            return reservation.modify(arrivalDate, departureDate);
        }

        public Reservation cancelReservation(Reservation reservation) {
            return reservation.cancel();
        }

        public static TestCase whenDepartureDateSameAsArrivalDate() {
            final LocalDate now = LocalDate.now();
            return new TestCase(now, now);
        }

        public static TestCase whenValidDuration(int startOffset) {
            final int duration = randomValidDuration();
            final LocalDate arrivalDate = LocalDate.now().plusDays(startOffset);
            final LocalDate departureDate = arrivalDate.plusDays(duration);
            return new TestCase(arrivalDate, departureDate);
        }

        private static TestCase whenPeriodIsBackwards(int startOffset) {
            final int duration = randomValidDuration();
            final LocalDate arrivalDate = LocalDate.now().plusDays(startOffset);
            final LocalDate departureDate = arrivalDate.minusDays(duration);
            return new TestCase(arrivalDate, departureDate);
        }

        public static TestCase whenDurationIsTooLong(int daysOffset) {
            int duration = randomInvalidDuration();
            final LocalDate arrivalDate = LocalDate.now().plusDays(daysOffset);
            final LocalDate departureDate = arrivalDate.plusDays(duration);
            return new TestCase(arrivalDate, departureDate);
        }

        public static TestCase whenReservationStartsTooSoon() {
            int duration = randomValidDuration();
            final LocalDate arrivalDate = LocalDate.now();
            final LocalDate departureDate = arrivalDate.plusDays(duration);
            return new TestCase(arrivalDate, departureDate);
        }

        public static TestCase whenReservationStartsTooDistant(int monthsOffset) {
            int duration = randomValidDuration();
            final LocalDate arrivalDate = LocalDate.now().plusMonths(monthsOffset);
            final LocalDate departureDate = arrivalDate.plusDays(duration);
            return new TestCase(arrivalDate, departureDate);
        }

        public static TestCase whenReservationStartsTomorrow() {
            int duration = randomValidDuration();
            final LocalDate arrivalDate = LocalDate.now().plusDays(1);
            final LocalDate departureDate = arrivalDate.plusDays(duration);
            return new TestCase(arrivalDate, departureDate);
        }

        public static TestCase whenReservationStartsInOneMonth() {
            int duration = randomValidDuration();
            final LocalDate arrivalDate = LocalDate.now().plusMonths(1);
            final LocalDate departureDate = arrivalDate.plusDays(duration);
            return new TestCase(arrivalDate, departureDate);
        }

        public void assertThatReservationWasCreatedAsExpected(Reservation reservation) {
            assertEquals(bookingIdentifier, reservation.getBookingIdentifier());
            assertEquals(creationDate, reservation.getCreationDate());
            assertEquals(arrivalDate, reservation.getArrivalDate());
            assertEquals(departureDate, reservation.getDepartureDate());
            assertEquals(user, reservation.getUser());
            assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
        }

        public void assertThatReservationWasUpdatedAsExpected(Reservation reservation, Reservation updatedReservation) {
            assertEquals(reservation.getBookingIdentifier(), updatedReservation.getBookingIdentifier());
            assertEquals(reservation.getCreationDate(), updatedReservation.getCreationDate());
            assertEquals(arrivalDate, updatedReservation.getArrivalDate());
            assertEquals(departureDate, updatedReservation.getDepartureDate());
            assertEquals(reservation.getUser(), updatedReservation.getUser());
            assertEquals(reservation.getStatus(), updatedReservation.getStatus());
        }

        public void assertThatReservationWasCancelledAsExpected(Reservation cancelledReservation) {
            assertEquals(bookingIdentifier, cancelledReservation.getBookingIdentifier());
            assertEquals(creationDate, cancelledReservation.getCreationDate());
            assertEquals(arrivalDate, cancelledReservation.getArrivalDate());
            assertEquals(departureDate, cancelledReservation.getDepartureDate());
            assertEquals(user, cancelledReservation.getUser());
            assertEquals(ReservationStatus.CANCELLED, cancelledReservation.getStatus());
            assertTrue(cancelledReservation.isCancelled());
        }

        public void assertContainsReservedDate(List<ReservedDate> reservedDates, ReservedDate reservedDate) {
            assertTrue(reservedDates.stream()
                                    .filter(r -> r.getBookingIdentifier().equals(reservedDate.getBookingIdentifier()) && r.getDate()
                                                                                                                          .equals(r.getDate()))
                                    .findFirst()
                                    .isPresent());
        }

        public void assertContainsReservedDates(List<ReservedDate> reservedDates) {
            final Long duration = arrivalDate.until(departureDate, ChronoUnit.DAYS);
            assertEquals(duration, reservedDates.size());
            LongStream.range(0, duration)
                      .boxed()
                      .map(i -> new ReservedDate(arrivalDate.plusDays(i), bookingIdentifier))
                      .forEach(reservedDate -> assertContainsReservedDate(reservedDates, reservedDate));
        }
    }
}
