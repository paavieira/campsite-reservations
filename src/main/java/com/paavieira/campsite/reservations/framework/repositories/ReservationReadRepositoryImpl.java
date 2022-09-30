package com.paavieira.campsite.reservations.framework.repositories;

import com.paavieira.campsite.reservations.application.repositories.ReservationReadRepository;
import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.domain.ReservationStatus;
import com.paavieira.campsite.reservations.domain.exceptions.ReservationNotFound;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservationDocument;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class ReservationReadRepositoryImpl implements ReservationReadRepository {

    private final MongoTemplate mongoTemplate;

    public ReservationReadRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Reservation findActive(String bookingIdentifier) {
        final Query query = query(new Criteria().andOperator(isActive(), id(bookingIdentifier)));
        return Optional.ofNullable(mongoTemplate.findOne(query, ReservationDocument.class))
                       .map(ReservationDocument::toDomain)
                       .orElseThrow(() -> new ReservationNotFound(bookingIdentifier));
    }

    @Override
    public List<Reservation> listActive(LocalDate from, LocalDate to) {
        final Query query = query(new Criteria().andOperator(isActive(), reservationOverlapWith(from, to)));
        return mongoTemplate.find(query, ReservationDocument.class)
                            .stream()
                            .map(ReservationDocument::toDomain)
                            .collect(Collectors.toList());
    }

    private Criteria id(String bookingIdentifier) {
        return where("_id").is(bookingIdentifier);
    }

    private Criteria reservationOverlapWith(LocalDate from, LocalDate to) {
        // given two periods (StartA, EndA), (StartB, EndB)
        // them they overlap if StartA < EndB and EndA > StartB
        return where("arrivalDate").lt(to).and("departureDate").gt(from);
    }

    private Criteria isActive() {
        return where("status").is(ReservationStatus.ACTIVE);
    }

}
