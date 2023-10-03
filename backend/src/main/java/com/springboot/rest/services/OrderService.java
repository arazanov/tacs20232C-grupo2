package com.springboot.rest.services;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.repositories.OrderRepository;
import com.springboot.rest.repositories.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository = new OrderRepository();

    public Order createOrder(int userId) {
        User user = new UserService().getUserById(userId);
        if(user == null){
            return null;
        }
        Order order = new Order(user);
        order.setId(orderRepository.maxId());
        return orderRepository.save(order);
    }

    public List<Order> getOrderList() {
        return orderRepository.findAll();
    }

    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getOrdersByUserId(int id) {
        return orderRepository.findByUserId(id);
    }

    public Order addItem(int id, int userId, Item item) {
        Order order = orderRepository.findById(id).orElse(createOrder(userId));
        if(order == null) {
            return null;
        }
        User user = new UserService().getUserById(userId);
        order.addItems(user, item);
        return order;
    }

    public Order shareOrder(int id, User user) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) order.shareWith(user);
        return order;
    }

    public Order closeOrder(int id, int userId, boolean close) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            User user = new UserService().getUserById(userId);
            if (order.isTheCreator(user)) order.changeStatus(user, close);
        }
        return order;
    }

    public void deleteOrderById(int id) {
        orderRepository.deleteById(id);
    }
}
