import model.ItemType;
import model.Monitor;
import model.Order;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class OrderTest {
    private User user1, user2, user3;
    private ItemType empanadaCarne, empanadaVerdura, pizzaNapo, pizzaFuga;

    @Before
    public void init() {
        empanadaCarne = new ItemType(
                1,
                "empanada de carne",
                "empanada de carne cortada a cuchillo con cebolla",
                200
        );
        empanadaVerdura = new ItemType(
                2,
                "empanada de verdura",
                "empanada de acelga con ricota",
                200
        );
        pizzaNapo = new ItemType(
                3,
                "pizza napolitana",
                "muzzarella, tomates en rodajas, ajo",
                1200
        );
        pizzaFuga = new ItemType(
                4,
                "pizza fugazzeta",
                "muzzarella y cebolla caramelizada",
                1100
        );

        user1 = new User("pepe");
        user2 = new User("carla");
        user3 = new User("alex");
    }

    @Test
    public void placeAnOrder() {
        Order order1 = new Order(user1);

        order1.shareWith(user2, user3);

        order1.addItems(user1, empanadaCarne, 6);
        order1.addItems(user2, pizzaNapo, 1);
        order1.addItems(user3, empanadaCarne, 2);

        order1.close(user1);

        order1.addItems(user2, empanadaVerdura, 3);

        // ítems y cantidades del pedido
        System.out.println("ítems: ");
        order1.getItems().forEach(i -> {
            System.out.println("\t" + i.getItemType().getName());
            System.out.println("\t" + i.getQuantity());
            System.out.println("\t" + i.getItemType().getDescription());
            System.out.println("\t" + i.calculatePrice());
            System.out.println();
        });
        System.out.println();

        // trazabilidad
        order1.getActions().forEach(a -> System.out.println(a.getDescription()));
        System.out.println();

        // contador de pedidos
        System.out.println("Pedidos creados: " + Monitor.getInstance().getOrdersCreated());
        System.out.println("Usuarios que interactuaron: " + Monitor.getInstance().getUniqueUsers());

        Assert.assertEquals(1200 + 8 * 200, order1.getTotalPrice(), 0);
    }
}
