import java.util.*;

public class Monitor {
    private static Monitor instance = null;

    private Monitor() {
        this.orders = new HashSet<>();
        this.users = new HashSet<>();
    }

    public static Monitor getInstance() {
        if (instance == null) instance = new Monitor();
        return instance;
    }

    private Set<Order> orders;
    private Set<User> users;

    public void beNotified(User user, Order order) {
        orders.add(order);
        users.add(user);
    }

    public int uniqueUsers() {
        return users.size();
    }

    public int ordersCreated() {
        return orders.size();
    }

}