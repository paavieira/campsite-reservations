package com.paavieira.campsite.reservations.framework.repositories.mongo;

import com.paavieira.campsite.reservations.domain.ReservedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("reserveddates")
public class ReservedDateDocument {
    @Id
    private LocalDate date;

    private String bookingIdentifier;

    public ReservedDateDocument(LocalDate date, String bookingIdentifier) {
        this.date = date;
        this.bookingIdentifier = bookingIdentifier;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBookingIdentifier() {
        return bookingIdentifier;
    }

    public void setBookingIdentifier(String bookingIdentifier) {
        this.bookingIdentifier = bookingIdentifier;
    }

    public static ReservedDateDocument fromDomain(ReservedDate reservedDate) {
        return new ReservedDateDocument(reservedDate.getDate(), reservedDate.getBookingIdentifier());
    }

    public ReservedDate toDomain() {
        return new ReservedDate(date, bookingIdentifier);
    }

}