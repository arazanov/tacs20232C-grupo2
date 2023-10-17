package com.springboot.rest.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Action {

    public Action() {
    }

    public Action(User user, String description) {
        this.user = user;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.description = user.getUsername() + description;
    }

    @DBRef
    private User user;
    private String dateTime;
    private String description;

    public String getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

}