import lombok.Getter;

@Getter
public class Item {

    public Item(ItemType itemType, int quantity) {
        this.quantity = quantity;
        this.itemType = itemType;
    }

    private ItemType itemType;
    private int quantity;

    public void addItems(int amount) {
        quantity += amount;
    }

    public String getName() {
        return itemType.getName();
    }

    public double calculatePrice() {
        return quantity * itemType.getPrice();
    }

}