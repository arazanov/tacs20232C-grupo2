package com.springboot.rest.controllers;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.security.services.CustomUserDetails;
import com.springboot.rest.services.ItemService;
import com.springboot.rest.services.OrderService;
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

    @GetMapping
    public List<Order> getOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return findOrThrow(orderService::findByUserId, userDetails.id());
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable String id) {
        return findOrThrow(orderService::findById, id);
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
    public ResponseEntity<Order> createOrder() {
        Order newOrder = orderService.save(new Order());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/{id}")
                .buildAndExpand(newOrder.getId())
                .toUri();

        return ResponseEntity.created(location).body(newOrder);
    }

    @PostMapping("/{id}/items")
    public Item createItem(@PathVariable String id) {
        Item item = new Item();
        Order order = findOrThrow(orderService::findById, id);
        item.setOrder(order);
        return itemService.save(item);
    }

    @PatchMapping("/{id}")
    public void modifyOrder(@PathVariable String id, @RequestBody OrderPatchRequest request,
                            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Order order = findOrThrow(orderService::findById, id);

        if (request.description() != null) {
            order.setDescription(request.description());
        }

        if (request.closed() != null) {
            if (order.isOwner(userDetails.id()))
                order.setClosed(request.closed());
            else throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "User is not the owner");
        }

        if (request.user() != null) {
            order.addUser(request.user());
        }

        orderService.update(order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteById(id);
        itemService.deleteByOrderId(id);
    }

    public record OrderPatchRequest(String description, Boolean closed, User user) {
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
