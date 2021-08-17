package com.bluntsoftware.qovery.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.qovery.model.Person;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends MongoRepository<Person, String> {
}