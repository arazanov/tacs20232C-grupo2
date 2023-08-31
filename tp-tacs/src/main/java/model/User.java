package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "User")
public class User {

    public User(String username) {
        this.username = username;
    }

    private final String username;

    public String getUsername() {
        return username;
    }

}