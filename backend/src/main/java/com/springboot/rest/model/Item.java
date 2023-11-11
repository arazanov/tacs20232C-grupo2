package com.springboot.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document
public class Item {

    public Item() {
        this.version = 0;
        this.description = "Agregar descripción";
        this.quantity = 0;
    }

    @Id
    private String id;
    private int version;
    private String description;
    private int quantity;
    private String unit;

    @DBRef
    @JsonIgnore
    private Order order;

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public boolean isUpToDate(int version) {
        return this.version == version;
    }

    public void incrementVersion() {
        version += 1;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}