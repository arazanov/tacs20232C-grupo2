import model.Item;
import model.Monitor;
import model.Order;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderTest {
    private User user1, user2, user3;
    private Item empanadaCarne, empanadaVerdura, pizzaNapolitana, pizzaFugazzeta;

    @Before
    public void init() {
        empanadaCarne = new Item("empanada de carne");
        empanadaVerdura = new Item("empanada de verdura");
        pizzaNapolitana = new Item("pizza napolitana");
        pizzaFugazzeta = new Item("pizza fugazzeta");

        user1 = new User("pepe");
        user2 = new User("carla");
        user3 = new User("alex");
    }

    @Test
    public void placeAnOrder() {
        Order order1 = new Order(user1);

        order1.shareWith(user2, user3);

        order1.addItems(user1, empanadaCarne, 6);
        order1.addItems(user2, pizzaNapolitana, 2);
        order1.removeItems(user1, pizzaNapolitana, 1);
        order1.addItems(user3, empanadaCarne, 2);

        order1.close(user1);

        order1.addItems(user2, empanadaVerdura, 3);

        // ítems y cantidades del pedido
        System.out.println("Ítems: ");
        order1.getItems().forEach(i -> System.out.println("\tdescripción: " + i.getDescription() + "\tcantidad: " + i.getQuantity()));

        System.out.println("\nAcciones: ");

        // trazabilidad
        order1.getActions().forEach(a -> System.out.println("\t" + a.getDescription()));
        System.out.println();

        // contador de pedidos
        System.out.println("Pedidos creados: " + Monitor.getInstance().getOrdersCreated());
        System.out.println("Usuarios que interactuaron: " + Monitor.getInstance().getUniqueUsers());

        Assert.assertEquals(2, order1.getItems().size(), 0);
    }
}
