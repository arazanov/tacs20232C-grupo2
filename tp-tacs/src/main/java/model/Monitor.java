package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

@XmlRootElement(name = "Monitor")
public class Monitor {
    private static Monitor instance = null;

    public Monitor() {
        this.orders = new HashSet<>();
        this.users = new HashSet<>();
    }

    public static Monitor getInstance() {
        if (instance == null) instance = new Monitor();
        return instance;
    }

    private final Set<Order> orders;
    private final Set<User> users;

    public void beNotified(User user, Order order) {
        orders.add(order);
        users.add(user);
    }

    public int getUniqueUsers() {
        return users.size();
    }

    public int getOrdersCreated() {
        return orders.size();
    }

}