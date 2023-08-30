import lombok.Getter;

@Getter
public class ItemType {

    public ItemType(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    private String name;
    private String description;
    private double price;

}