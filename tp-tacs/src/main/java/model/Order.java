package model;

import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "order")
public class Order {

    public Order(User user) {
        this.user = user;
        this.items = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.closed = false;
        this.actions.add(new Creation(user, this));
    }

    private User user;
    private List<Item> items;
    @Getter
    private List<Action> actions;
    @Getter
    private boolean closed;
    private Double totalPrice;

    public void addItems(User user, ItemType itemType, int quantity) {
        if(isClosed()) return;

        Optional<Item> itemOptional = items.stream().filter(i -> i.getItemType().equals(itemType)).findFirst();

        if (itemOptional.isPresent()) itemOptional.get().addItems(quantity);
        else items.add(new Item(itemType, quantity));

        actions.add(new Change(
                user,
                this,
                itemType.getName(),
                quantity
        ));
    }

    public void close(User user) {
        if(isClosed()) return;
        if(this.user.equals(user)) this.closed = true;
    }

    public Map<String, Integer> items() {
        HashMap<String, Integer> map = new HashMap<>();
        items.forEach(i -> map.put(i.getName(), i.getQuantity()));
        return map;
    }

    public double calculatePrice() {
        if(isClosed()) {
            if(totalPrice == null) totalPrice = items.stream().mapToDouble(Item::calculatePrice).reduce(0, Double::sum);
            return totalPrice;
        }
        return items.stream().mapToDouble(Item::calculatePrice).reduce(0, Double::sum);
    }

}