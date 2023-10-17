package com.springboot.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Document(collection = "orders")
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

    @Id
    private String id;
    private String description;
    @DBRef
    @JsonIgnore
    private User user;
    @DBRef
    @JsonIgnore
    private Set<User> users;
    @JsonIgnore
    private List<Item> items;
    @JsonIgnore
    private List<Action> actions;
    private boolean closed;

    public void shareWith(User user) {
        this.users.add(user);
        actions.add(new Action(this.user, " shared the order with " + user.getUsername()));
    }

    public void shareWith(List<User> users) {
        this.users.addAll(users);
        actions.add(new Action(this.user, " shared the order with "
                + users.stream().map(User::getUsername).collect(Collectors.joining(", "))));
    }

    public void addItems(User user, Item item) {
        String description = item.getDescription();
        int quantity = item.getQuantity();
        Optional<Item> itemOptional = find(description);

        if (itemOptional.isPresent()) itemOptional.get().addItems(quantity);
        else items.add(item);

        actions.add(new Action(user, " added " + quantity + " '" + description + "'"));
        Monitor.getInstance().userInteraction(user);
    }

    public boolean hasUser(String id) {
        if (user.getId().equals(id)) return true;
        return users.stream().anyMatch(e -> e.getId().equals(id));
    }

    public void removeItems(User user, Item item) {
        String description = item.getDescription();

        find(description).ifPresent(i -> {
            int quantity = item.getQuantity();
            if (quantity > i.getQuantity()) quantity = i.getQuantity();
            i.removeItems(quantity);

            actions.add(new Action(user, " removed " + quantity + " '" + description + "'"));
            Monitor.getInstance().userInteraction(user);
        });
    }

    public void changeStatus(User user, boolean close) {
        this.closed = close;
        if (close) actions.add(new Action(user, " closed the order"));
        else actions.add(new Action(user, " reopened the order"));
    }

    public boolean isTheCreator(User user) {
        return this.user.getUsername().equals(user.getUsername());
    }

    public boolean hasNoPermission(User user) {
        return users.stream().noneMatch(u -> u.getUsername().equals(user.getUsername()));
    }

    private Optional<Item> find(String description) {
        return items.stream().filter(i -> i.getDescription().equals(description)).findFirst();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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