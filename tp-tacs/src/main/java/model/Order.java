package model;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "Order")
public class Order {

    public Order(User user) {
        this.user = user;
        this.items = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.closed = false;
        this.actions.add(new Action(user, this, " created an order"));
    }

    private final User user;
    private final List<Item> items;
    private final List<Action> actions;
    private boolean closed;
    private Double totalPrice;

    private Optional<Item> findById(int id) {
        return items.stream().filter(i -> i.getItemType().getId() == id).findFirst();
    }

    @POST
    public Response addItems(User user, ItemType itemType, int quantity) {
        if(isClosed()) return Response.status(Response.Status.UNAUTHORIZED).build();

        Optional<Item> itemOptional = findById(itemType.getId());

        if (itemOptional.isPresent()) itemOptional.get().addItems(quantity);
        else items.add(new Item(itemType, quantity));

        actions.add(new Action(
                user,
                this,
                " added " + quantity + " \"" + itemType.getName() + "\""
        ));
        return Response.ok().build();
    }

    @DELETE
    public Response deleteItem(int id) {
        findById(id).ifPresent(i -> items.remove(id));
        return Response.ok().build();
    }

    public void close(User user) {
        if(isClosed()) return;
        if(this.user.equals(user)) this.closed = true;
    }

    public double calculatePrice() {
        if(isClosed()) {
            if(totalPrice == null) totalPrice = items.stream().mapToDouble(Item::calculatePrice).reduce(0, Double::sum);
            return totalPrice;
        }
        return items.stream().mapToDouble(Item::calculatePrice).reduce(0, Double::sum);
    }

    // Getters

    public User getUser() {
        return user;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Action> getActions() {
        return actions;
    }

    public boolean isClosed() {
        return closed;
    }

    public Double getTotalPrice() {
        return calculatePrice();
    }

}