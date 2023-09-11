package com.model;

import org.springframework.stereotype.Component;

@Component
public class User {

    public User() {
    }

    public User(String username) {
        this.username = username;
        this.neverInteracted = true;
    }

    private int id;
    private String username;
    private String password;
    private boolean neverInteracted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean neverInteracted() {
        return neverInteracted;
    }

    public void interact() {
        neverInteracted = false;
    }

}