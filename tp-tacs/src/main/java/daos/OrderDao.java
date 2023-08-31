package daos;

import model.ItemType;
import model.Order;
import model.User;

public class OrderDao extends Dao<Order> {
    public OrderDao() {
        User user1 = new User("pepe");
        User user2 = new User("carla");
        ItemType empanadaCarne = new ItemType(1, "empanada de carne", "empanada de carne cortada a cuchillo con cebolla", 200);
        ItemType pizzaNapo = new ItemType(3, "pizza napolitana", "muzzarella, tomates en rodajas, ajo", 1200);

        Order order1 = new Order(user1);

        order1.addItems(user1, empanadaCarne, 6);
        order1.addItems(user2, pizzaNapo, 1);
        order1.addItems(user2, empanadaCarne, 2);
        order1.close(user1);

        elements.put(1, order1);
    }
}
