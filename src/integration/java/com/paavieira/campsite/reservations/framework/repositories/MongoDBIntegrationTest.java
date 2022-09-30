package com.paavieira.campsite.reservations.framework.repositories;

import com.paavieira.campsite.reservations.domain.Reservation;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservationDocument;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservationMongoRepository;
import com.paavieira.campsite.reservations.framework.repositories.mongo.ReservedDateMongoRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

@Testcontainers
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class MongoDBIntegrationTest {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.1");

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected ReservationMongoRepository reservationMongoRepository;

    @Autowired
    protected ReservedDateMongoRepository reservedDateMongoRepository;

    @AfterEach
    public void tearDown() {
        deleteAll();
    }
    protected void save(Reservation... reservations) {
        Arrays.stream(reservations)
              .map(ReservationDocument::fromDomain)
              .forEach(reservationMongoRepository::insert);
    }

    protected void deleteAll() {
        reservationMongoRepository.deleteAll();
        reservedDateMongoRepository.deleteAll();
    }

}
