package com.springboot.rest.payload;

import com.springboot.rest.model.User;

public class OrderPatchRequest {

    private String description;
    private Boolean closed;
    private User user;

    public String getDescription() {
        return description;
    }

    public Boolean isClosed() {
        return closed;
    }

    public User getUser() {
        return user;
    }

}