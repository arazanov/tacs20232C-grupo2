package com.services;

import com.model.Order;
import com.model.User;
import com.repositories.OrderRepository;
import com.repositories.Repository;
import com.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    public Repository<User> userRepository() {
        return new UserRepository();
    }

    @Bean
    public Repository<Order> orderRepository() {
        return new OrderRepository();
    }
}
