package model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Change implements Action {

    public Change(User user, Order order, String itemName, int quantity) {
        this.user = user;
        this.order = order;
        this.dateTime = LocalDateTime.now();
        this.description = user.getUsername() + " added " + quantity + " " + itemName + " on " + dateTime.format(DateTimeFormatter.ISO_DATE);
        notify(Monitor.getInstance());
    }

    private User user;
    private Order order;
    private LocalDateTime dateTime;
    @Getter
    private String description;

    @Override
    public void notify(Monitor monitor) {
        monitor.beNotified(user, order);
    }

}