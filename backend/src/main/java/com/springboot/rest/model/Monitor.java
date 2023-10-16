package com.springboot.rest.model;

import org.springframework.stereotype.Component;

@Component
public class Monitor {

    public Monitor() {
        this.uniqueUsers = 0;
        this.ordersCreated = 0;
    }

    public static Monitor getInstance() {
        if (instance == null) instance = new Monitor();
        return instance;
    }

    private static Monitor instance = null;
    private int uniqueUsers;
    private int ordersCreated;

    public void orderCreated(User user) {
        ordersCreated++;
        userInteraction(user);
    }

    public void userInteraction(User user) {
        if(user.neverInteracted()) {
            user.interact();
            uniqueUsers++;
        }
    }

    public int getUniqueUsers() {
        return uniqueUsers;
    }

    public int getOrdersCreated() {
        return ordersCreated;
    }

}