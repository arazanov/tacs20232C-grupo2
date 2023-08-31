package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "User")
public class User {

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    @XmlElement
    private String username;

    // Getter

    public String getUsername() {
        return username;
    }

}