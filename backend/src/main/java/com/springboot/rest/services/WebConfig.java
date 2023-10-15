package com.springboot.rest.services;

import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.repositories.OrderRepository;
import com.springboot.rest.repositories.Repository;
import com.springboot.rest.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

   /* @Bean
    public Repository<User> userRepository() {
        return new UserRepository();
    }

    @Bean
    public Repository<Order> orderRepository() {
        return new OrderRepository();
    }*/
}
