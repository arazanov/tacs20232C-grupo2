package model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Creation implements Action {

    public Creation(User user, Order order) {
        this.user = user;
        this.order = order;
        this.dateTime = LocalDateTime.now();
        this.description = "order created by " + user.getUsername() + " on " + dateTime.format(DateTimeFormatter.ISO_DATE);
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