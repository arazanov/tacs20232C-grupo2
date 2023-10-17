package com.springboot.rest.repositories;

import com.springboot.rest.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    @Query("{ 'user.id' : '?0' }")
    List<Order> findByUserId(String id);

}