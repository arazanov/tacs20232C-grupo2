package com.springboot.rest.services;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    public Order createOrder(String userId) {
        User user = userService.getUserById(userId);
        if(user == null) {
            return null;
        }
        Order order = new Order(user);
        order.setId(orderRepository.maxId());
        return orderRepository.save(order);
    }

    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getOrdersByUserId(String id) {
        return orderRepository.findByUserId(id);
    }

    public Order changeDescription(String id, String description) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null)
            order.setDescription(description);
        return order;
    }

    public Order addItem(String id, String userId, Item item) {
        Order order = orderRepository.findById(id).orElse(createOrder(userId));
        if(order == null) {
            return null;
        }
        User user = userService.getUserById(userId);
        order.addItems(user, item);
        return order;
    }

    public Order shareOrder(String id, User user) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) order.shareWith(user);
        return order;
    }

    public Order changeStatus(String id, String userId, boolean close) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            User user = userService.getUserById(userId);
            if (order.isTheCreator(user)) order.changeStatus(user, close);
        }
        return order;
    }

    public Order updateOrder(Order order) {
        return orderRepository.updateById(order);
    }

    public void deleteOrderById(String id) {
        orderRepository.deleteById(id);
    }
}
