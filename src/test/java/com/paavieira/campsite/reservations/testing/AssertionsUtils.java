package com.paavieira.campsite.reservations.testing;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.User;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservationDocument;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservedDateDocument;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.stream.IntStream;

public class AssertionsUtils {

    public static void assertEquals(ReservationDocument expected, ReservationDocument actual) {
        Assertions.assertEquals(expected.getBookingIdentifier(), actual.getBookingIdentifier());
        Assertions.assertEquals(expected.getCreationDate(), actual.getCreationDate());
        Assertions.assertEquals(expected.getArrivalDate(), actual.getArrivalDate());
        Assertions.assertEquals(expected.getDepartureDate(), actual.getDepartureDate());
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getUser(), actual.getUser());
    }

    public static void assertEquals(List<ReservedDateDocument> expected, List<ReservedDateDocument> actual) {
        Assertions.assertEquals(expected.size(), actual.size());
        IntStream.range(0, expected.size()).forEach(i -> assertEquals(expected.get(i), actual.get(i)));
    }

    public static void assertEquals(ReservedDateDocument expected, ReservedDateDocument actual) {
        Assertions.assertEquals(expected.getBookingIdentifier(), actual.getBookingIdentifier());
        Assertions.assertEquals(expected.getDate(), actual.getDate());
    }

    public static void assertEquals(User expected, User actual) {
        Assertions.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
        Assertions.assertEquals(expected.getEmail(), actual.getEmail());
    }

    public static void assertEquals(Reservation expected, Reservation actual) {
        Assertions.assertEquals(expected.getBookingIdentifier(), actual.getBookingIdentifier());
        Assertions.assertEquals(expected.getCreationDate(), actual.getCreationDate());
        Assertions.assertEquals(expected.getArrivalDate(), actual.getArrivalDate());
        Assertions.assertEquals(expected.getDepartureDate(), actual.getDepartureDate());
        Assertions.assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getUser(), actual.getUser());
    }
}
