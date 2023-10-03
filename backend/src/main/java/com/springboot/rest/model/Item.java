package com.springboot.rest.model;

import org.springframework.stereotype.Component;

@Component
public class Item {

    public Item() {
    }

    public Item(String description, int quantity) {
        this.description = description;
        this.quantity = quantity;
    }

    private int id;

    private String description;
    private int quantity;

    public void addItems(int quantity) {
        this.quantity += quantity;
    }

    public void removeItems(int quantity) {
        this.quantity -= quantity;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}