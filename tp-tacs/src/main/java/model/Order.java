package model;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.*;

@XmlRootElement(name = "Order")
public class Order {

    public Order() {
    }

    public Order(User user) {
        this.user = user;
        this.users = new HashSet<>();
        this.items = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.closed = false;
        this.actions.add(new Action(user, this, " created an order"));
    }

    private User user;
    private Set<User> users;
    private List<Item> items;
    private List<Action> actions;
    private boolean closed;
    private Double totalPrice;

    public void shareWith(User ... users) {
        Collections.addAll(this.users, users);
    }

    @POST
    public Response addItems(User user, ItemType itemType, int quantity) {
        if(isClosed() || !user.equals(this.user) && !users.contains(user))
            return Response.status(Response.Status.UNAUTHORIZED).build();

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
    public Response removeItem(int id) {
        findById(id).ifPresent(i -> items.remove(id));
        return Response.ok().build();
    }

    public void close(User user) {
        if(isClosed()) return;
        if(this.user.equals(user)) this.closed = true;
        actions.add(new Action(user, this, " closed the order"));
    }

    private Optional<Item> findById(int id) {
        return items.stream().filter(i -> i.getItemType().getId() == id).findFirst();
    }

    private double calculatePrice() {
        return items.stream().mapToDouble(Item::calculatePrice).reduce(0, Double::sum);
    }

    // Getters

    public User getUser() {
        return user;
    }

    public Set<User> getUsers() {
        return users;
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
        if(isClosed()) {
            if(totalPrice == null) totalPrice = calculatePrice();
            return totalPrice;
        }
        return calculatePrice();
    }

}