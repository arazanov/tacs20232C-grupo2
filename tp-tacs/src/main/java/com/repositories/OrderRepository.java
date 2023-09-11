package com.repositories;

import com.model.Item;
import com.model.Order;
import com.model.User;

import java.util.Optional;

public class OrderRepository extends Repository<Order> {

    public OrderRepository() {
        User pepe = new User("pepe");
        pepe.setId(1);
        User carla = new User("carla");
        carla.setId(2);
        User alex = new User("alex");
        alex.setId(3);

        Order order1 = new Order(pepe);
        order1.setId(1);

        order1.shareWith(carla);

        order1.addItems(pepe, new Item("empanada de carne", 6));
        order1.addItems(carla, new Item("pizza napolitana", 2));
        order1.removeItems(pepe, new Item("pizza napolitana", 1));
        order1.addItems(alex, new Item("empanada de carne", 2));

        order1.changeStatus(pepe, true);

        entities.add(order1);
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
