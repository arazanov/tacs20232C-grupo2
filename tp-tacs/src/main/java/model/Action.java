package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "Action")
public class Action {

    public Action() {
    }

    public Action(User user, Order order, String description) {
        this.user = user;
        this.order = order;
        this.dateTime = LocalDateTime.now();
        this.description = user.getUsername() + description + " on " + dateTime.format(DateTimeFormatter.ISO_DATE);
        notify(Monitor.getInstance());
    }

    @XmlElement
    private User user;
    @XmlElement
    private Order order;
    @XmlElement
    private LocalDateTime dateTime;
    @XmlElement
    private String description;

    public void notify(Monitor monitor) {
        monitor.beNotified(user, order);
    }

}