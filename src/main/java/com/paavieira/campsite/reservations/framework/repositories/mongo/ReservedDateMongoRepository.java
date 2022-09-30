package com.paavieira.campsite.reservations.framework.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservedDateMongoRepository extends MongoRepository<ReservedDateDocument, String> {
}
