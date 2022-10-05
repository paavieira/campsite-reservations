package com.paavieira.campsite.reservations.framework.repositories;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.exceptions.ReservationNotFound;
import com.paavieira.campsite.reservations.testing.AssertionsUtils;
import com.paavieira.campsite.reservations.testing.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

public class ReservationReadRepositoryImplIntegrationTest extends MongoDBIntegrationTest {
    private ReservationReadRepositoryImpl repository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void setup() {
        repository = new ReservationReadRepositoryImpl(mongoTemplate);
    }

    @Test
    public void findActive_withValidBookingIdentifier_thenReturnsExpectedReservation() {
        final FindActiveTestCase testCase = new FindActiveTestCase();
        save(testCase.reservation);
        final Reservation actual = repository.findActive(testCase.bookingIdentifier);
        AssertionsUtils.assertEquals(testCase.reservation, actual);
    }

    @Test
    public void findActive_withNonExistingBookingIdentifier_thenThrowsReservationNotFound() {
        final FindActiveTestCase testCase = new FindActiveTestCase();
        save(testCase.reservation);
        Assertions.assertThrows(ReservationNotFound.class, () -> repository.findActive(RandomUtils.randomString()));
    }

    @Test
    public void findActive_withCancelledReservation_thenThrowsReservationNotFound() {
        final FindActiveTestCase testCase = new FindActiveTestCase();
        save(testCase.reservation.cancel());
        Assertions.assertThrows(ReservationNotFound.class, () -> repository.findActive(RandomUtils.randomString()));
    }

    @Test
    public void listActive_whenQueryWithFullPeriod_thenReturnsAllReservations() {
        final ListActiveTestCase testCase = new ListActiveTestCase();
        save(testCase.reservation1, testCase.reservation2, testCase.reservation3);
        final LocalDate from = testCase.arrivalDate1;
        final LocalDate to = testCase.departureDate3;
        final List<Reservation> actual = repository.listActive(from, to);
        testCase.assertEquals(actual);
    }

    @Test
    public void listActive_whenQueryWithRestrictedPeriod_thenReturnsExpectedReservation() {
        final ListActiveTestCase testCase = new ListActiveTestCase();
        save(testCase.reservation1, testCase.reservation2, testCase.reservation3);
        final LocalDate from = testCase.arrivalDate1;
        final LocalDate to = testCase.departureDate1;
        final List<Reservation> actual = repository.listActive(from, to);
        testCase.assertOnly(actual, testCase.reservation1);
    }

    @Test
    public void listActive_whenQueryWithBadPeriod_thenReturnsEmpty() {
        final ListActiveTestCase testCase = new ListActiveTestCase();
        save(testCase.reservation1, testCase.reservation2, testCase.reservation3);
        final LocalDate from = LocalDate.now().minusMonths(1);
        final LocalDate to = from.plusDays(1);
        final List<Reservation> actual = repository.listActive(from, to);
        testCase.assertEmpty(actual);
    }

    @Test
    public void listActive_whenQueryWithFullPeriod_andReservationsAreCancelled_thenReturnsEmpty() {
        final ListActiveTestCase testCase = new ListActiveTestCase();
        save(testCase.reservation1.cancel(), testCase.reservation2.cancel(), testCase.reservation3.cancel());
        final LocalDate from = testCase.arrivalDate1;
        final LocalDate to = testCase.departureDate3;
        final List<Reservation> actual = repository.listActive(from, to);
        testCase.assertEmpty(actual);
    }


    private static class FindActiveTestCase {
        private final Reservation reservation;
        private final String bookingIdentifier;

        public FindActiveTestCase() {
            this.reservation = RandomUtils.randomValidReservation();
            this.bookingIdentifier = reservation.getBookingIdentifier();
        }
    }

    private static class ListActiveTestCase {
        private final Reservation reservation1, reservation2, reservation3;
        private final List<Reservation> reservations;
        private final LocalDate arrivalDate1, arrivalDate2, arrivalDate3;
        private final LocalDate departureDate1, departureDate2, departureDate3;

        public ListActiveTestCase() {
            this.arrivalDate1 = LocalDate.now().plusDays(RandomUtils.randomInt(1, 3));
            this.departureDate1 = arrivalDate1.plusDays(RandomUtils.randomInt(1, 3));
            this.arrivalDate2 = departureDate1.plusDays(RandomUtils.randomInt(1, 3));
            this.departureDate2 = arrivalDate2.plusDays(RandomUtils.randomInt(1, 3));
            this.arrivalDate3 = departureDate2.plusDays(RandomUtils.randomInt(1, 3));
            this.departureDate3 = arrivalDate3.plusDays(RandomUtils.randomInt(1, 3));
            this.reservation1 = RandomUtils.randomValidReservation().modify(arrivalDate1, departureDate1);
            this.reservation2 = RandomUtils.randomValidReservation().modify(arrivalDate2, departureDate2);
            this.reservation3 = RandomUtils.randomValidReservation().modify(arrivalDate3, departureDate3);
            this.reservations = List.of(reservation1, reservation2, reservation3);
        }

        public void assertEquals(List<Reservation> reservations) {
            Assertions.assertEquals(this.reservations.size(), reservations.size());
            IntStream.range(0, this.reservations.size())
                     .forEach(i -> AssertionsUtils.assertEquals(this.reservations.get(i), reservations.get(i)));
        }

        public void assertOnly(List<Reservation> actual, Reservation reservation) {
            Assertions.assertEquals(1, actual.size());
            AssertionsUtils.assertEquals(reservation, actual.get(0));
        }

        public void assertEmpty(List<Reservation> actual) {
            Assertions.assertTrue(actual.isEmpty());
        }
    }
}
