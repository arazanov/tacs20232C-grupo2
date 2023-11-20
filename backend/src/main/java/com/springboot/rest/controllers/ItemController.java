package com.springboot.rest.controllers;

import com.springboot.rest.model.Item;
import com.springboot.rest.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/items/{id}")
@PreAuthorize("hasRole('USER')")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public Item getItem(@PathVariable String id) {
        return itemService.findById(id);
    }

    @PutMapping
    public void updateItem(@PathVariable String id, @RequestBody Item update) {
        Item item = itemService.findById(id);

        if (item.isClosed())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Order is closed");

        if (!update.isUpToDate(item.getVersion()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Item is not up to date");

        item.incrementVersion();
        item.setDescription(update.getDescription());
        item.setQuantity(update.getQuantity());
        item.setUnit(update.getUnit());
        itemService.update(item);
    }

    @DeleteMapping
    public void deleteItem(@PathVariable String id) {
        itemService.deleteById(id);
    }

}