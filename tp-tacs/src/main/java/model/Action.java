package model;

import lombok.Getter;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "action")
public class Action {

    public Action(User user, Order order, String description) {
        this.user = user;
        this.order = order;
        this.dateTime = LocalDateTime.now();
        this.description = user.getUsername() + description + " on " + dateTime.format(DateTimeFormatter.ISO_DATE);
        notify(Monitor.getInstance());
    }

    private User user;
    private Order order;
    private LocalDateTime dateTime;
    @Getter
    private String description;

    public void notify(Monitor monitor) {
        monitor.beNotified(user, order);
    }

}