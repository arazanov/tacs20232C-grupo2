package daos;

import model.Item;
import model.Order;
import model.User;

public class OrderDao extends Dao<Order> {
    public OrderDao() {
        User user1 = new User("pepe");
        User user2 = new User("carla");
        User user3 = new User("alex");

        Item empanadaCarne = new Item("empanada de carne");
        Item pizzaNapolitana = new Item("pizza napolitana");

        Order order1 = new Order(user1);

        order1.shareWith(user2, user3);

        order1.addItems(user1, empanadaCarne, 6);
        order1.addItems(user2, pizzaNapolitana, 2);
        order1.removeItems(user1, pizzaNapolitana, 1);
        order1.addItems(user3, empanadaCarne, 2);

        order1.close(user1);

        entities.put(1, order1);
    }
}
