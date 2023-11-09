package com.springboot.rest.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Document(collection = "actions")
public class Action {

    public Action() {
    }

    public Action(String userId, String orderId, String description) {
        this.userId = userId;
        this.orderId = orderId;
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.description = description;
    }

    private String userId;
    private String orderId;
    private String dateTime;
    private String description;

}