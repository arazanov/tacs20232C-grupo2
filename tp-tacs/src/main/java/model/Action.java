package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "Action")
public class Action {

    public Action(User user, Order order, String description) {
        this.user = user;
        this.order = order;
        this.dateTime = LocalDateTime.now();
        this.description = user.getUsername() + description + " on " + dateTime.format(DateTimeFormatter.ISO_DATE);
        notify(Monitor.getInstance());
    }

    private final User user;
    private final Order order;
    private final LocalDateTime dateTime;
    private final String description;

    public void notify(Monitor monitor) {
        monitor.beNotified(user, order);
    }

    // Getters

    public User getUser() {
        return user;
    }

    public String getDateTime() {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public String getDescription() {
        return description;
    }

}