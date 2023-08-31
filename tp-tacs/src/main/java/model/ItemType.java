package model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ItemType")
public class ItemType {

    public ItemType() {
    }

    public ItemType(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    private int id;
    private String name;
    private String description;
    private double price;

    // Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

}