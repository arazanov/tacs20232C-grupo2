package model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "User")
public class User {

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    private String username;

    public String getUsername() {
        return username;
    }

}