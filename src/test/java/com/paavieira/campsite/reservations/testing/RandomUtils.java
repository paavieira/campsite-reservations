package com.paavieira.campsite.reservations.testing;

import com.paavieira.campsite.reservations.domain.Availability;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.ReservationStatus;
import com.paavieira.campsite.reservations.domain.User;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

public class RandomUtils {

    public static int randomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static LocalDate randomDateInTheFuture() {
        return randomDateAfter(LocalDate.now());
    }

    public static LocalDate randomDateAfter(LocalDate date) {
        return randomDateAfter(date, 1, 3);
    }

    public static LocalDate randomDateBefore(LocalDate date) {
        return randomDateBefore(date, 1, 3);
    }

    public static LocalDate randomDateAfter(LocalDate date, int minDays, int maxDays) {
        return date.plusDays(randomInt(minDays, maxDays));
    }

    public static LocalDate randomDateBefore(LocalDate date, int minDays, int maxDays) {
        return date.minusDays(randomInt(minDays, maxDays));
    }

    public static String randomEmail() {
        return "%s@%s.com".formatted(randomString(5), randomString(5));
    }

    public static String randomString() {
        return randomString(randomInt(3, 10));
    }

    public static String randomString(int size) {
        byte[] array = new byte[size];
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }

    public static User randomUser() {
        return new User(randomString(), randomString(), randomEmail());
    }

    public static int randomValidDuration() {
        return randomInt(1, 3);
    }

    public static int randomInvalidDuration() {
        return randomInt(4, 10);
    }

    public static Long randomVersion() { return Long.valueOf(randomInt(0, 10));}

    public static Reservation randomValidReservation() {
        final String bookingIdentifier = randomId();
        final LocalDate creationDate = LocalDate.now();
        final LocalDate arrivalDate = randomDateInTheFuture();
        final LocalDate departureDate = arrivalDate.plusDays(randomInt(1, 3));
        final ReservationStatus status = ReservationStatus.ACTIVE;
        final User user = randomUser();
        return Reservation.create(bookingIdentifier, creationDate, arrivalDate, departureDate, status, user, Reservation.INITIAL_VERSION);
    }

    public static Availability randomAvailability() {
        final LocalDate from = randomDateInTheFuture();
        final LocalDate to = from.plusDays(randomInt(1, 3));
        return new Random().nextBoolean() ? Availability.available(from, to) : Availability.unavailable(from, to);
    }
}
