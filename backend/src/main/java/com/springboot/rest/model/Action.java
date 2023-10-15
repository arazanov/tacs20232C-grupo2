package com.springboot.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Document(collection = "actions")
public class Action {

    public Action() {
    }

    public Action(User user, String description) {
        this.user = user;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.description = user.getUsername() + description;
    }
    @Id
    private String id;
    @DBRef
    private User user;
    @Field("dateTime")
    private String dateTime;
    @Field("description")
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