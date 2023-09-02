package model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "Action")
public class Action {

    public Action() {
    }

    public Action(User user, String description) {
        this.user = user;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.description = user.getUsername() + description;
    }

    private User user;
    private String dateTime;
    private String description;

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