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

    public void shareWith(User ... users) {
        Collections.addAll(this.users, users);
    }

    public void addItems(User user, Item item, int quantity) {
        if(unauthorized(user)) return;
        Monitor.getInstance().userInteraction(user);

        Optional<Item> itemOptional = find(item);

        if (itemOptional.isPresent()) itemOptional.get().addItems(quantity);
        else {
            item.addItems(quantity);
            items.add(item);
        }

        actions.add(new Action(user, " added " + quantity + " \"" + item.getDescription() + "\""));
    }

    public void removeItems(User user, Item item, int quantity) {
        if(unauthorized(user)) return;
        Monitor.getInstance().userInteraction(user);

        find(item).ifPresent(i -> {
            i.removeItems(quantity);
            actions.add(new Action(user, " removed " + quantity + " \"" + item.getDescription() + "\""));
        });
    }

    public void close(User user) {
        if(isClosed()) return;
        if(this.user.equals(user)) this.closed = true;
        actions.add(new Action(user, " closed the order"));
    }

    private Optional<Item> find(Item item) {
        return items.stream().filter(i -> i.equals(item)).findFirst();
    }

    private boolean unauthorized(User user) {
        return isClosed() || !user.equals(this.user) && !users.contains(user);
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