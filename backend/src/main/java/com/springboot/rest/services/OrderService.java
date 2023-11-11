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

    public Order save(Order order) {
        return orderRepository.save(order);
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

    private interface Updater {
        void apply(Order order);
    }

    private void update(Order order, Updater updater) {
        updater.apply(order);
        orderRepository.deleteById(order.getId());
        orderRepository.save(order);
    }

    public void changeDescription(Order order, String description) {
        update(order, o -> o.setDescription(description));
    }

    public void changeStatus(Order order, Boolean status) {
        update(order, o -> o.setClosed(status));
    }

    public void shareOrder(Order order, User user) {
        update(order, o -> o.addUser(user));
    }

    public void deleteById(String id) {
        orderRepository.deleteById(id);
    }

    public void deleteByUserId(String userId) {
        orderRepository.deleteByUserId(userId);
    }

}
