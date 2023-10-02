package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String password;
    @JsonIgnore
    private boolean neverInteracted = true;

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

    public boolean isNeverInteracted() {
        return neverInteracted;
    }

    public void interact() {
        neverInteracted = false;
    }

}