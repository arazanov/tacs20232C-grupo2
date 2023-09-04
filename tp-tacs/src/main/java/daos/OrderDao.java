package daos;

import model.Order;
import model.User;

import java.util.Arrays;

public class OrderDao extends Dao<Order> {
    public OrderDao() {
        User user1 = new User("pepe");
        User user2 = new User("carla");
        User user3 = new User("alex");

        Order order1 = new Order(user1);

        order1.shareWith(Arrays.asList(user2));

        order1.addItems(user1, "empanada de carne", 6);
        order1.addItems(user2, "pizza napolitana", 2);
        order1.removeItems(user1, "pizza napolitana", 1);
        order1.addItems(user3, "empanada de carne", 2);

        order1.close(user1);

        entities.put(1, order1);
    }
}
