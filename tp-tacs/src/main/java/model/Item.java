package model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
public class Item {

    public Item() {
    }

    public Item(String description) {
        this.description = description;
        this.quantity = 0;
    }

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

}