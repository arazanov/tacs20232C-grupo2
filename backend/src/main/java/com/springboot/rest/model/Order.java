package com.springboot.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Document(collection = "orders")
public class Order {

    public Order() {
        this.description = "Agregar descripción";
        this.users = new HashSet<>();
        this.closed = false;
    }

    @Id
    private String id;
    private String description;
    @DBRef
    private User user;
    @DBRef
    @JsonIgnore
    private Set<User> users;
    private boolean closed;
    @Transient
    private boolean owned;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean isOwned() {
        return owned;
    }

    public boolean isOwner(String userId) {
        return userId.equals(user.getId());
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setOwned(String userId) {
        this.owned = this.user.getId().equals(userId);
    }

}