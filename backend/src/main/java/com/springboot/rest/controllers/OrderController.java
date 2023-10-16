package com.springboot.rest.controllers;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.payload.ErrorMessage;
import com.springboot.rest.payload.OrderState;
import com.springboot.rest.security.services.UserDetailsImpl;
import com.springboot.rest.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasRole('USER')")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private String getUserId() {
        UserDetailsImpl userDetails =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    private ResponseEntity<?> orderNotFound(String id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("orden", id));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getOrdersByUserId(getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        if (order != null) return ResponseEntity.ok().body(order);
        else return orderNotFound(id);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<?> getItemsByOrder(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        if (order != null) return ResponseEntity.ok().body(order.getItems());
        else return orderNotFound(id);
    }

    @GetMapping("/{id}/actions")
    public ResponseEntity<?> getActionsByOrder(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        if (order != null) return ResponseEntity.ok().body(order.getActions());
        else return orderNotFound(id);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getUsersByOrder(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        if (order != null) return ResponseEntity.ok().body(order.getUsers());
        else return orderNotFound(id);
    }

    @PostMapping
    public ResponseEntity<?> addOrder() {
        String userId = getUserId();
        Order newOrder = orderService.createOrder(userId);
        if (newOrder != null) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/{id}")
                    .buildAndExpand(newOrder.getId())
                    .toUri();
            return ResponseEntity.created(location).body(newOrder);
        } else
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErrorMessage("usuario", userId));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<?> addItem(@PathVariable String id, @RequestBody Item item) {
        String userId = getUserId();
        Order order = orderService.addItem(id, userId, item);
        if (order != null) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/orders/{id}/items")
                    .buildAndExpand(order.getId())
                    .toUri();
            return ResponseEntity.created(location).body(order);
        } else
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErrorMessage("orden+usuario", id, userId));
    }

/*
    @PatchMapping("/{id}/")
    public ResponseEntity<Order> shareOrder(@PathVariable String id, @RequestBody User user) {
        System.out.println("ENTRAA");
        return ResponseEntity.ok().body(orderService.shareOrder(id, user));
    }
*/
    // Los usuarios compartidos entran en el put mapping, se actualiza toda la orden completa

    @PatchMapping("/{id}")
    public ResponseEntity<Order> changeOrderState(@PathVariable String id, @RequestBody OrderState orderState) {
        String userId = getUserId();
        if(orderState.isClosed() != null)
            return ResponseEntity.ok().body(orderService.changeStatus(id, userId, orderState.isClosed()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping
    public ResponseEntity<Order> updateOrder(@RequestBody Order order) {
        return ResponseEntity.ok().body(orderService.updateOrder(order));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrderById(id);
    }
    
}
