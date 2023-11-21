package com.springboot.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Document
public class User {

    public User() {
        this.active = false;
    }

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    private boolean active;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object that) {
        return that == this || that instanceof User && Objects.equals(id, ((User) that).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}