package model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
public class Item {

    public Item() {
    }

    public Item(ItemType itemType, int quantity) {
        this.quantity = quantity;
        this.itemType = itemType;
    }

    private ItemType itemType;
    private int quantity;

    public void addItems(int quantity) {
        this.quantity += quantity;
    }

    public double calculatePrice() {
        return quantity * itemType.getPrice();
    }

    // Getters

    public ItemType getItemType() {
        return itemType;
    }

    public int getQuantity() {
        return quantity;
    }

}