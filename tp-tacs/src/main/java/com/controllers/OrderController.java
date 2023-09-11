package com.controllers;

import com.model.Item;
import com.model.Order;
import com.model.User;
import com.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getOrderList());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        return ResponseEntity.ofNullable(orderService.getOrderById(id));
    }

    @GetMapping("/orders/{id}/items")
    public ResponseEntity<List<Item>> getItemsByOrderById(@PathVariable int id) {
        return ResponseEntity.ok().body(orderService.getOrderById(id).getItems());
    }

    @PostMapping("/orders/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Order> addOrder(@PathVariable int userId) {
        return ResponseEntity.ok(orderService.createOrder(userId));
    }

    @PostMapping("/orders/{id}/{userId}")
    public ResponseEntity<Order> addItem(@PathVariable int id, @PathVariable int userId, @RequestBody Item item) {
        return ResponseEntity.ok(orderService.addItem(id, userId, item));
    }

    @PatchMapping("/orders/{id}")
    public ResponseEntity<Order> shareOrder(@PathVariable int id, @RequestBody User user) {
        return ResponseEntity.ok().body(orderService.shareOrder(id, user));
    }

    @PatchMapping("/orders/{id}/{userId}")
    public ResponseEntity<Order> closeOrder(@PathVariable int id, @PathVariable int userId, @RequestBody Order order) {
        return ResponseEntity.ok().body(orderService.closeOrder(id, userId, order.isClosed()));
    }

    @DeleteMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable int id) {
        orderService.deleteOrderById(id);
    }
    
}
