package com.springboot.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
@Document(collection = "monitor")
public class Monitor {

    public Monitor() {
        id = "0";
        userCount = 0;
        orderCount = 0;
    }

    @Id
    private String id;
    private int userCount;
    private int orderCount;

    public void incrementUserCount() {
        userCount += 1;
    }

    public void incrementOrderCount() {
        orderCount += 1;
    }

    public int getUserCount() {
        return userCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

}