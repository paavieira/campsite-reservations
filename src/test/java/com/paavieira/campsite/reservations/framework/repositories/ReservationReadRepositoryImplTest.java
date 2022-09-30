package com.paavieira.campsite.reservations.framework.repositories;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservationDocument;
import com.paavieira.campsite.reservations.testing.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

public class ReservationReadRepositoryImplTest {

    private ReservationReadRepositoryImpl repository;
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setup() {
        mongoTemplate = Mockito.mock(MongoTemplate.class);
        repository = new ReservationReadRepositoryImpl(mongoTemplate);
    }

    @Test
    public void findActive_thenRunsExpectedQuery() {
        final FindActiveTestCase testCase = new FindActiveTestCase();
        Mockito.when(mongoTemplate.findOne(Mockito.any(Query.class), Mockito.eq(ReservationDocument.class)))
               .thenReturn(ReservationDocument.fromDomain(testCase.reservation));
        repository.findActive(testCase.bookingIdentifier);
        final ArgumentCaptor<Query> captor = ArgumentCaptor.forClass(Query.class);
        Mockito.verify(mongoTemplate, Mockito.times(1)).findOne(captor.capture(), Mockito.eq(ReservationDocument.class));
        final Query query = captor.getValue();
        testCase.assertThatFiltersById(query);
        testCase.assertThatFiltersByStatus(query);
    }

    @Test
    public void listActive_thenRunsExpectedQuery() {
        final ListActiveTestCase testCase = new ListActiveTestCase();
        Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(ReservationDocument.class)))
               .thenReturn(List.of(ReservationDocument.fromDomain(testCase.reservation)));
        repository.listActive(testCase.from, testCase.to);
        final ArgumentCaptor<Query> captor = ArgumentCaptor.forClass(Query.class);
        Mockito.verify(mongoTemplate, Mockito.times(1)).find(captor.capture(), Mockito.eq(ReservationDocument.class));
        final Query query = captor.getValue();
        testCase.assertThatFiltersByStatus(query);
        testCase.assertThatFiltersByPeriod(query);
    }

    private static class FindActiveTestCase {
        private final Reservation reservation;
        private final String bookingIdentifier;

        public FindActiveTestCase() {
            this.reservation = RandomUtils.randomValidReservation();
            this.bookingIdentifier = reservation.getBookingIdentifier();
        }

        private String trimQuery(Query query) {
            return query.toString().replaceAll("\\s", "");
        }

        public void assertThatFiltersById(Query query) {
            final String actual = trimQuery(query);
            final String expected = "{\"_id\":\"%s\"}".formatted(bookingIdentifier);
            Assertions.assertTrue(actual.contains(expected));
        }

        private void assertThatFiltersByStatus(Query query) {
            final String actual = trimQuery(query);
            final String expected = "{\"status\":{\"$java\":%s}}".formatted(reservation.getStatus());
            Assertions.assertTrue(actual.contains(expected));
        }
    }

    private static class ListActiveTestCase {
        private final Reservation reservation;
        private final LocalDate from;
        private final LocalDate to;

        public ListActiveTestCase() {
            this.from = RandomUtils.randomDateInTheFuture();
            this.to = RandomUtils.randomDateAfter(from);
            this.reservation = RandomUtils.randomValidReservation();
        }

        private String trimQuery(Query query) {
            return query.toString().replaceAll("\\s", "");
        }

        private void assertThatFiltersByStatus(Query query) {
            final String actual = trimQuery(query);
            final String expected = "{\"status\":{\"$java\":%s}}".formatted(reservation.getStatus());
            Assertions.assertTrue(actual.contains(expected));
        }

        private void assertThatFiltersByPeriod(Query query) {
            final String actual = trimQuery(query);
            final String expected = "{\"arrivalDate\":{\"$lt\":{\"$java\":%s}},\"departureDate\":{\"$gt\":{\"$java\":%s}}}".formatted(to,
                                                                                                                                      from);
            Assertions.assertTrue(actual.contains(expected));
        }

    }
}
