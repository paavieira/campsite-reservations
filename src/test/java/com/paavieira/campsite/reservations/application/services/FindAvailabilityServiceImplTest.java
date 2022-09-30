package com.paavieira.campsite.reservations.application.services;

import com.paavieira.campsite.reservations.application.repositories.ReservationReadRepository;
import com.paavieira.campsite.reservations.domain.Availability;
import com.paavieira.campsite.reservations.domain.queries.AvailabilityQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.paavieira.campsite.reservations.testing.RandomUtils.randomDateInTheFuture;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomInt;
import static com.paavieira.campsite.reservations.testing.RandomUtils.randomValidReservation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FindAvailabilityServiceImplTest {

    private FindAvailabilityServiceImpl service;
    private ReservationReadRepository repository;

    @BeforeEach
    public void setup() {
        repository = mock(ReservationReadRepository.class);
        service = new FindAvailabilityServiceImpl(repository);
    }

    @Test
    public void findAvailability_whenAvailable_thenReturnsExpectedAvailability() {
        final TestCase testCase = new TestCase();
        when(repository.listActive(testCase.from, testCase.to)).thenReturn(Collections.emptyList());
        final Availability availability = service.findAvailability(testCase.query);
        verify(repository, times(1)).listActive(testCase.from, testCase.to);
        testCase.assertIsAvailable(availability);
    }

    @Test
    public void findAvailability_whenUnavailable_thenReturnsExpectedAvailability() {
        final TestCase testCase = new TestCase();
        when(repository.listActive(testCase.from, testCase.to)).thenReturn(List.of(randomValidReservation()));
        final Availability availability = service.findAvailability(testCase.query);
        verify(repository, times(1)).listActive(testCase.from, testCase.to);
        testCase.assertIsUnavailable(availability);
    }

    private static class TestCase {
        private final LocalDate from;
        private final LocalDate to;
        private final AvailabilityQuery query;

        public TestCase() {
            this.from = randomDateInTheFuture();
            this.to = from.plusDays(randomInt(1, 10));
            this.query = new AvailabilityQuery(from, to);
        }

        public void assertIsAvailable(Availability availability) {
            Assertions.assertEquals(from, availability.getFrom());
            Assertions.assertEquals(to, availability.getTo());
            Assertions.assertTrue(availability.isAvailable());
        }

        public void assertIsUnavailable(Availability availability) {
            Assertions.assertEquals(from, availability.getFrom());
            Assertions.assertEquals(to, availability.getTo());
            Assertions.assertFalse(availability.isAvailable());
        }
    }
}
