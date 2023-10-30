package com.springboot.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Document(collection = "users")
public class User {

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>(){{ add("ROLE_USER"); }};
    }

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    @Transient
    @JsonIgnore
    private String token;
    @JsonIgnore
    private Set<String> roles;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public boolean neverInteracted() {
        return neverInteracted;
    }

    public void interact() {
        neverInteracted = false;
    }

}