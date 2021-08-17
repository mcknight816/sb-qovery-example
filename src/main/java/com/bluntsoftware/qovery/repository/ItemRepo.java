package com.bluntsoftware.qovery.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.bluntsoftware.qovery.model.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends MongoRepository<Item, String> {
}