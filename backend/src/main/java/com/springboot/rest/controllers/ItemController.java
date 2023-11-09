package com.springboot.rest.controllers;

import com.springboot.rest.model.Item;
import com.springboot.rest.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public void updateItem(@PathVariable String id, @RequestBody Item item) {
        itemService.update(id, item);
    }

    @DeleteMapping
    public void deleteItem(@PathVariable String id) {
        itemService.deleteById(id);
    }

}