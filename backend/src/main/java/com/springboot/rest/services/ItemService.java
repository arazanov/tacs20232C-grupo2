package com.springboot.rest.services;

import com.springboot.rest.model.Item;
import com.springboot.rest.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Item findById(String id) {
        return itemRepository.findById(id).orElseThrow();
    }

    public List<Item> findByOrderId(String orderId) {
        return itemRepository.findByOrderId(orderId);
    }

    public void update(String id, Item item) {
        Item update = findById(id);
        if (!item.isUpToDate(update.getVersion()))
            throw new RuntimeException("Item is not up to date");

        update.incrementVersion();
        update.setDescription(item.getDescription());
        update.setQuantity(item.getQuantity());
        update.setUnit(item.getUnit());

        itemRepository.deleteById(id);
        itemRepository.save(update);
    }

    public void deleteById(String id) {
        itemRepository.deleteById(id);
    }

    public void deleteByOrderId(String orderId) {
        itemRepository.deleteByOrderId(orderId);
    }

}
