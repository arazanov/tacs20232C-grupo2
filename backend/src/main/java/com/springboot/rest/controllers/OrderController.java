package com.springboot.rest.controllers;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.payload.OrderPatchRequest;
import com.springboot.rest.security.services.UserDetailsImpl;
import com.springboot.rest.services.ItemService;
import com.springboot.rest.services.OrderService;
import com.springboot.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@PreAuthorize("hasRole('USER')")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;


    private String userId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    private interface Finder<T> {
        T findBy(String id);
    }

    private <T> T getById(Finder<T> finder, String id) {
        try {
            return finder.findBy(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El pedido con ID " + id + " no se encontró en el sistema", e);
        }
    }

    @GetMapping
    public List<Order> getOrders() {
        Finder<List<Order>> finder = (userId) -> orderService.findByUserId(userId);
        return getById(finder, userId());
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable String id) {
        Finder<Order> finder = (orderId) -> orderService.findById(orderId);
        return getById(finder, id);
    }

    @GetMapping("/{id}/users")
    public Set<User> getUsers(@PathVariable String id) {
        Finder<Set<User>> finder = (orderId) -> orderService.findUsersById(orderId);
        return getById(finder, id);
    }

    @GetMapping("/{id}/items")
    public List<Item> getItems(@PathVariable String id) {
        Finder<List<Item>> finder = (orderId) -> itemService.findByOrderId(orderId);
        return getById(finder, id);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder() {
        String userId = userId();
        try {
            Order newOrder = new Order();

            newOrder.setUser(userService.findById(userId));
            orderService.save(newOrder);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/{id}")
                    .buildAndExpand(newOrder.getId())
                    .toUri();

            return ResponseEntity.created(location).body(newOrder);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El usuario con ID " + userId + " no se encontró en el sistema", e);
        }
    }

    @PostMapping("/{id}/items")
    public Item createItem(@PathVariable String id) {
        Item item = new Item();
        item.setOrder(orderService.findById(id));
        return itemService.save(item);
    }

    @PatchMapping("/{id}")
    public void modifyOrder(@PathVariable String id, @RequestBody OrderPatchRequest request) {
        try {
            orderService.modify(id, request, userId());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteById(id);
        itemService.deleteByOrderId(id);
    }

}
