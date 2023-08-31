package model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "Action")
public class Action {

    public Action() {
    }

    public Action(User user, Order order, String description) {
        this.user = user;
        this.order = order;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.description = user.getUsername() + description + " on " + this.dateTime;
        notify(Monitor.getInstance());
    }

    private User user;
    private Order order;
    private String dateTime;
    private String description;

    public void notify(Monitor monitor) {
        monitor.beNotified(user, order);
    }

    // Getters

    public User getUser() {
        return user;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

}