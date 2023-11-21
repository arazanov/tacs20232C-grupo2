package com.springboot.rest.controllers;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.User;
import com.springboot.rest.security.services.CustomUserDetails;
import com.springboot.rest.services.ItemService;
import com.springboot.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/items/{id}")
@PreAuthorize("hasRole('USER')")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @GetMapping
    public Item getItem(@PathVariable String id) {
        return itemService.findById(id);
    }

    @PutMapping
    public void updateItem(@PathVariable String id, @RequestBody Item update,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Item item = itemService.findById(id);

        if (!userDetails.isActive()) {
            User user = userDetails.getUser();
            user.setActive(true);
            userService.save(user);
        }
        
        if (item.isClosed())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Order is closed");

        if (!update.isUpToDate(item.getVersion()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Item is not up to date");

        item.incrementVersion();
        item.setDescription(update.getDescription());
        item.setQuantity(update.getQuantity());
        item.setUnit(update.getUnit());
        itemService.save(item);
    }

    @DeleteMapping
    public void deleteItem(@PathVariable String id) {
        Item item = itemService.findById(id);
        if (item.isClosed())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Order is closed");
        itemService.deleteById(id);
    }

}