package com.springboot.rest.repositories;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        order1.setDescription("pizzas con los chicos");
        order1.shareWith(carla);

        order1.addItems(pepe, new Item("empanada de carne", 6));
        order1.addItems(carla, new Item("pizza napolitana", 2));
        order1.removeItems(pepe, new Item("pizza napolitana", 1));
        order1.addItems(alex, new Item("empanada de carne", 2));

        order1.changeStatus(pepe, true);
        entities.add(order1);

        Order order2 = new Order(pepe);
        order2.setId(2);
        order2.setDescription("asado semi Libertadores");
        order2.shareWith(Arrays.asList(carla, alex));

        order2.addItems(pepe, new Item("vacío", 2));
        order2.addItems(carla, new Item("matambre", 2));
        order2.addItems(alex, new Item("chimichurri", 1));
        order2.addItems(alex, new Item("ensalada mixta", 2));

        order1.changeStatus(pepe, true);
        entities.add(order2);
    }

    @Override
    public int getId(Order order) {
        return order.getId();
    }

    public List<Order> findByUserId(int id){
        return entities.stream().filter(e -> e.hasUser(id)).collect(Collectors.toList());
    }

}
