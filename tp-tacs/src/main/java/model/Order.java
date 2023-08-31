package model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "Order")
public class Order {

    public Order() {
    }

    public Order(User user) {
        this.user = user;
        this.items = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.closed = false;
        this.actions.add(new Action(user, this, " created an order"));
    }

    @XmlElement
    private User user;
    private List<Item> items;
    private List<Action> actions;
    @XmlElement
    private boolean closed;
    @XmlElement
    private Double totalPrice;

    private Optional<Item> findById(int id) {
        return items.stream().filter(i -> i.getId() == id).findFirst();
    }

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

    public Response deleteItem(int id) {
        findById(id).ifPresent(i -> items.remove(id));
        return Response.ok().build();
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

    // Getters

    public User getUser() {
        return user;
    }

    @GET
    @Path("items")
    public List<Item> getItems() {
        return items;
    }

    @GET
    @Path("actions")
    public List<Action> getActions() {
        return actions;
    }

    public boolean isClosed() {
        return closed;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }
}