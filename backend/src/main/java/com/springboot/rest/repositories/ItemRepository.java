package com.springboot.rest.repositories;

import com.springboot.rest.model.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ItemRepository extends MongoRepository<Item, String> {

    List<Item> findByOrderId(String orderId);

    void deleteByOrderId(String orderId);

}