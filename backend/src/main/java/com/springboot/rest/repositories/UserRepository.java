package com.springboot.rest.repositories;

import com.springboot.rest.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("{'$or': [{'username': ?0}, {'email': ?0}]}")
    Optional<User> findByUsernameOrEmail(String username);

    int countAllByActiveIsTrue();

}