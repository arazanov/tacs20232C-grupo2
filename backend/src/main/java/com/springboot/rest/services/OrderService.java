package com.springboot.rest.services;

import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.payload.OrderPatchRequest;
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

    private void update(Order order) {
        orderRepository.deleteById(order.getId());
        orderRepository.save(order);
    }

    public void modify(String id, OrderPatchRequest request, String userId) throws Exception {
        Order order = findById(id);

        // setDescription
        if (request.getDescription() != null) {
            order.setDescription(request.getDescription());
        }

        // close
        if (request.isClosed() != null) {
            if (order.isOwner(userId)) {
                order.setClosed(request.isClosed());
            }
            else throw new Exception("User is not the owner");
        }

        // share
        if (request.getUser() != null) {
            order.addUser(request.getUser());
        }

        update(order);
    }

    public void deleteById(String id) {
        orderRepository.deleteById(id);
    }

    public void deleteByUserId(String userId) {
        orderRepository.deleteByUserId(userId);
    }

}
