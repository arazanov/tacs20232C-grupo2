package model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name = "user")
public class User {

    public User(String username) {
        this.username = username;
    }

    private String username;

}