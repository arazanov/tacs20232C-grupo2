package com.repositories;

import com.model.Item;
import com.model.Order;
import com.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderRepository extends Repository<Order> {

    public OrderRepository() {
        User pepe = new User("pepe");
        pepe.setId(1);
        User carla = new User("carla");
        carla.setId(2);
        User alex = new User("alex");
        alex.setId(3);

        /*String[] nombres = {"juan", "maria", "lucas", "ana", "sergio", "laura", "diego"};
        List<User> users = new ArrayList<User>();

        for (int i = 3; i < 10; i++) {
            User usuario = new User(nombres[i]);
            usuario.setId(i + 1);
            users.add(usuario);
        }*/

        Order order1 = new Order(pepe);
        order1.setId(1);

        /*Order order2 = new Order(users.get(0));
        order2.setId(11);

        Order order3 = new Order(users.get(1));
        order3.setId(12);

        Order order4 = new Order(users.get(2));
        order4.setId(13);

        Order order5 = new Order(users.get(3));
        order5.setId(14);

        Order order6 = new Order(users.get(4));
        order6.setId(15);

        Order order7 = new Order(users.get(5));
        order7.setId(16);

        Order order8 = new Order(users.get(6));
        order8.setId(17);*/

        order1.shareWith(carla);

        order1.addItems(pepe, new Item("empanada de carne", 6));
        order1.addItems(carla, new Item("pizza napolitana", 2));
        order1.removeItems(pepe, new Item("pizza napolitana", 1));
        order1.addItems(alex, new Item("empanada de carne", 2));

        /*order2.addItems(users.get(1), new Item("Sushi de Salmón", 7));
        order3.addItems(users.get(2), new Item("Tacos al Pastor", 4));
        order4.addItems(users.get(3), new Item("Lasagna de Espinacas", 8));
        order5.addItems(users.get(4), new Item("Ensalada César", 3));
        order6.addItems(users.get(5), new Item("Burgers de Portobello", 9));
        order7.addItems(users.get(6), new Item("Sopa de Tomate Asado", 2));
        order8.addItems(users.get(7), new Item("Paella de Mariscos", 10));*/

        order1.changeStatus(pepe, true);

        entities.add(order1);
    }

    public List<Order> findByUserId(int id){
        return entities.stream().filter(e -> e.hasUser(id)).collect(Collectors.toList());
    }

    @Override
    public Optional<Order> findById(int id) {
        return entities.stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public int maxId() {
        return entities.stream().mapToInt(Order::getId).max().orElse(0) + 1;
    }

}
