package com.springboot.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

@Component
@Document(collection = "items")
public class Item {

    public Item() {
    }

    public Item(String description, int quantity) {
        this.description = description;
        this.quantity = quantity;
    }

    @Id
    private int id;
    @Field("description")
    private String description;
    @Field("quantity")
    private int quantity;

    public void addItems(int quantity) {
        this.quantity += quantity;
    }

    public void removeItems(int quantity) {
        this.quantity -= quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

}