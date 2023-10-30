package com.springboot.rest;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Monitor;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderTest {
    private User user1, user2, user3;

    @Before
    public void init() {
        user1 = new User("pepe", "pepe@gmail.com", "123");
        user2 = new User("carla", "carla@gmail.com", "123");
        user3 = new User("alex", "alex@gmail.com", "123");
    }

    @Test
    public void placeAnOrder() {
        Order order1 = new Order(user1);

        order1.shareWith(user2);
        order1.shareWith(user3);

        order1.addItems(user1, new Item("empanada de carne", 6));
        order1.addItems(user2, new Item("pizza napolitana", 2));
        order1.removeItems(user1, new Item("pizza napolitana", 1));
        order1.addItems(user3, new Item("empanada de carne", 2));

        order1.changeStatus(user1, true);

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
