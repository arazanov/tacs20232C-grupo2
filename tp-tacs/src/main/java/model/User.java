package model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "User")
public class User {

    public User() {
    }

    public User(String username) {
        this.username = username;
        this.neverInteracted = true;
    }

    private String username;
    private String password;
    private boolean neverInteracted;

    public String getUsername() {
        return username;
    }

    public boolean neverInteracted() {
        return neverInteracted;
    }

    public void interact() {
        neverInteracted = false;
    }

}