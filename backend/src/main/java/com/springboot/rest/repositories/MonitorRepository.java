package com.springboot.rest.repositories;

import com.springboot.rest.model.Monitor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MonitorRepository extends MongoRepository<Monitor, String> {
}
