package com.paavieira.campsite.reservations.framework.repositories.mongo;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.ReservationStatus;
import com.paavieira.campsite.reservations.domain.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("reservations")
public class ReservationDocument {

    @Id
    private String bookingIdentifier;
    private LocalDate creationDate;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private ReservationStatus status;
    private User user;
    @Version
    private Long version;

    public ReservationDocument(String bookingIdentifier,
                               LocalDate creationDate,
                               LocalDate arrivalDate,
                               LocalDate departureDate,
                               ReservationStatus status,
                               User user,
                               Long version) {
        this.bookingIdentifier = bookingIdentifier;
        this.creationDate = creationDate;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.status = status;
        this.user = user;
        this.version = version;
    }

    public static ReservationDocument fromDomain(com.paavieira.campsite.reservations.domain.Reservation reservation) {
        return new ReservationDocument(reservation.getBookingIdentifier(), reservation.getCreationDate(), reservation.getArrivalDate(),
                                       reservation.getDepartureDate(), reservation.getStatus(), reservation.getUser(), reservation.getVersion());
    }

    public String getBookingIdentifier() {
        return bookingIdentifier;
    }

    public void setBookingIdentifier(String bookingIdentifier) {
        this.bookingIdentifier = bookingIdentifier;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Reservation toDomain() {
        return Reservation.create(bookingIdentifier, creationDate, arrivalDate, departureDate, status, user, version);
    }
}
