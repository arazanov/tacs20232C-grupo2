package com.springboot.rest.model;

import org.springframework.stereotype.Component;

@Component
public class Monitor {

    public Monitor() {
    }

    public Monitor(long userCount, long orderCount) {
        this.userCount = userCount;
        this.orderCount = orderCount;
    }

    private long userCount;
    private long orderCount;

    public long getUserCount() {
        return userCount;
    }

    public long getOrderCount() {
        return orderCount;
    }
}