package com.springboot.rest.controllers;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.security.services.CustomUserDetails;
import com.springboot.rest.services.ItemService;
import com.springboot.rest.services.OrderService;
import com.springboot.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return findOrThrow(orderService::findByUserId, userDetails.id());
    }

    @GetMapping("/{id}")
    public Order getOrder(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable String id) {
        Order order=findOrThrow(orderService::findById, id);
        order.setOwned(userDetails.id());
        return order;
    }

    @GetMapping("/{id}/users")
    public Set<User> getUsers(@PathVariable String id) {
        return findOrThrow(orderService::findUsersById, id);
    }

    @GetMapping("/{id}/items")
    public List<Item> getItems(@PathVariable String id) {
        return findOrThrow(itemService::findByOrderId, id);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@AuthenticationPrincipal CustomUserDetails userDetails) {
        setActive(userDetails);

        Order newOrder = orderService.save(new Order());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/{id}")
                .buildAndExpand(newOrder.getId())
                .toUri();

        return ResponseEntity.created(location).body(newOrder);
    }

    @PostMapping("/{id}/items")
    public Item createItem(@PathVariable String id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Order order = findOrThrow(orderService::findById, id);
        setActive(userDetails);
        if (order.isClosed()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Order closed");
        }
        Item item = new Item();
        item.setOrder(order);
        return itemService.save(item);
    }

    @PatchMapping("/{id}")
    public void modifyOrder(@PathVariable String id, @RequestBody OrderRequest request,
                            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Order order = findOrThrow(orderService::findById, id);
        setActive(userDetails);

        if (request.description() != null) {
            if (order.isClosed())
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Order closed");
            if (!order.isUpToDate(request.version))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Order not up to date");
            order.incrementVersion();
            order.setDescription(request.description());
            orderService.save(order);
        }

        if (request.closed() != null) {
            if (order.isOwner(userDetails.id())) {
                order.setClosed(request.closed());
                orderService.save(order);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not the owner");
            }
        }

        if (request.user() != null) {
            if (order.isClosed())
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Order closed");
            if (request.user().equals(userDetails.getUser()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User is owner");
            order.addUser(request.user());
            orderService.save(order);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteById(id);
        itemService.deleteByOrderId(id);
    }

    @DeleteMapping("/{id}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable String id, @PathVariable String userId) {
        Order order = findOrThrow(orderService::findById, id);
        if (order.isClosed())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Order closed");
        order.removeUser(userId);
        orderService.save(order);
    }

    public record OrderRequest(int version, String description, Boolean closed, User user) {
    }

    private void setActive(CustomUserDetails userDetails) {
        if (!userDetails.isActive()) {
            User user = userDetails.getUser();
            user.setActive(true);
            userService.save(user);
        }
    }

    private interface Finder<T> {
        T findBy(String id);
    }

    private <T> T findOrThrow(Finder<T> finder, String id) {
        try {
            return finder.findBy(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El recurso con ID " + id + " no se encontró en el sistema");
        }
    }

}
