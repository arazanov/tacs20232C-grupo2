package model;

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

        this.actions.add(new Action(user, " created an order"));
        Monitor.getInstance().orderCreated(user);
    }

    private User user;
    private Set<User> users;
    private List<Item> items;
    private List<Action> actions;
    private boolean closed;

    public void shareWith(User user) {
        this.users.add(user);
        actions.add(new Action(this.user, " shared the order with " + user.getUsername()));
    }

    public void addItems(User user, Item item) {
        Monitor.getInstance().userInteraction(user);

        String description = item.getDescription();
        int quantity = item.getQuantity();
        Optional<Item> itemOptional = find(description);

        if (itemOptional.isPresent()) itemOptional.get().addItems(quantity);
        else items.add(item);

        actions.add(new Action(user, " added " + quantity + " \"" + description + "\""));
    }

    public void removeItems(User user, Item item) {
        Monitor.getInstance().userInteraction(user);

        String description = item.getDescription();
        int quantity = item.getQuantity();

        find(description).ifPresent(i -> {
            i.removeItems(quantity);
            actions.add(new Action(user, " removed " + quantity + " \"" + description + "\""));
        });
    }

    public void close(User user) {
        this.closed = true;
        actions.add(new Action(user, " closed the order"));
    }

    public void reopen(User user) {
        this.closed = false;
        actions.add(new Action(user, " reopened the order"));
    }

    public boolean isNotTheCreator(User user) {
        return !this.user.getUsername().equals(user.getUsername());
    }

    public boolean hasNoPermission(User user) {
        return users.stream().noneMatch(u -> u.getUsername().equals(user.getUsername()));
    }

    private Optional<Item> find(String description) {
        return items.stream().filter(i -> i.getDescription().equals(description)).findFirst();
    }

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

}