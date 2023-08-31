package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
public class Item {

    public Item() {
    }

    public Item(ItemType itemType, int quantity) {
        this.quantity = quantity;
        this.itemType = itemType;
    }

    @XmlElement
    private ItemType itemType;
    @XmlElement
    private int quantity;

    public void addItems(int amount) {
        quantity += amount;
    }

    public double calculatePrice() {
        return quantity * itemType.getPrice();
    }

    // Getters

    public int getId() {
        return itemType.getId();
    }

    public String getName() {
        return itemType.getName();
    }

    public int getQuantity() {
        return quantity;
    }

}