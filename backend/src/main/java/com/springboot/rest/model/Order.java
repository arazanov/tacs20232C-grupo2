package com.springboot.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Component
@Document
public class Order {

    public Order() {
        this.version = 0;
        this.description = "";
        this.closed = false;
        this.users = new HashSet<>();
    }

    @Id
    private String id;
    private int version;
    private String description;
    private boolean closed;

    @DBRef
    @CreatedBy
    private User user;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @DBRef
    @JsonIgnore
    private Set<User> users;

    @Transient
    private boolean owned;

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(String userId) {
        users.stream().filter(u -> u.getId().equals(userId)).findFirst().ifPresent(users::remove);
    }

    public int getVersion() {
        return version;
    }

    public boolean isUpToDate(int version) {
        return this.version == version;
    }

    public void incrementVersion() {
        version += 1;
    }

    public boolean isOwner(String userId) {
        return userId.equals(user.getId());
    }

    public boolean isOwned() {
        return owned;
    }

    public Set<User> getUsers() {
        return users;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setOwned(String userId) {
        this.owned = this.user.getId().equals(userId);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

}