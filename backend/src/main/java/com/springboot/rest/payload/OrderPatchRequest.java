package com.springboot.rest.payload;

import com.springboot.rest.model.User;

public class OrderPatchRequest {

    private String description;
    private Boolean closed;
    private User user;

    public boolean description() {
        return description != null;
    }

    public boolean closed() {
        return closed != null;
    }

    public boolean user() {
        return user != null;
    }

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