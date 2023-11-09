package com.springboot.rest.services;

import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void save(Order order) {
        orderRepository.save(order);
    }

    public Order findById(String id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public List<Order> findByUserId(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        orders.forEach(o -> o.setOwned(userId));
        return orders;
    }

    public Set<User> findUsersById(String id) {
        return orderRepository.findById(id).map(Order::getUsers).orElseThrow();
    }

    public long orderCount() {
        return orderRepository.count();
    }

    public void update(Order order) {
        orderRepository.deleteById(order.getId());
        orderRepository.save(order);
    }

    public void deleteById(String id) {
        orderRepository.deleteById(id);
    }

    public void deleteByUserId(String userId) {
        orderRepository.deleteByUserId(userId);
    }

}
