package com.springboot.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

@Component
@Document(collection = "users")
public class User {

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    @Id
    private String id;
    @Field("username")
    private String username;
    @Field("password")
    private String password;
    @Field("neverInteracted")
    @JsonIgnore
    private boolean neverInteracted = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNeverInteracted() {
        return neverInteracted;
    }

    public void interact() {
        neverInteracted = false;
    }

}