package com.springboot.rest;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.repositories.ItemRepository;
import com.springboot.rest.repositories.OrderRepository;
import com.springboot.rest.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
public class MongoTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Test
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }

    @Test
    public void saveUsers() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String[] names = {"pepe", "carla", "alex", "juan", "maria", "lucas", "ana", "sergio", "laura", "diego"};

        for (String name : names) {
            User user = new User(
                    name,
                    name + "@gmail.com",
                    passwordEncoder.encode("123")
            );
            userRepository.save(user);
        }

        Assert.assertEquals(10, userRepository.count());
    }

/*
    @Test
    public void saveOrders() {
        User pepe = userRepository.findByUsername("pepe").orElseThrow();
        User carla = userRepository.findByUsername("carla").orElseThrow();
        User alex = userRepository.findByUsername("alex").orElseThrow();

        Order order1 = new Order(pepe.getId());
        order1.setDescription("pizzas con los chicos");
        order1.shareWith(carla);
        order1 = orderRepository.save(order1);

        Item empanada = new Item(order1.getId()),
                pizza = new Item(order1.getId());

        empanada.setDescription("empanada de carne");
        empanada.setQuantity(6);
        pizza.setDescription("pizza napolitana");
        pizza.setQuantity(2);

        empanada = itemRepository.save(empanada);
        pizza = itemRepository.save(pizza);

        order1.removeItems(pepe, pizza);
        order1.addItems(alex, empanada);

        order1.changeStatus(true);
        orderRepository.save(order1);

        Order order2 = new Order(pepe);
        order2.setDescription("asado semi Libertadores");
        order2.shareWith(Arrays.asList(carla, alex));

        order2.addItems(pepe, new Item("vacío", 2));
        order2.addItems(carla, new Item("matambre", 2));
        order2.addItems(alex, new Item("chimichurri", 1));
        order2.addItems(alex, new Item("ensalada mixta", 2));

        orderRepository.save(order2);
        Assert.assertEquals(2, orderRepository.count());
    }
*/

/*    @Test
    public void resetDBWithExamples() {
        deleteAllOrders();
        deleteAllUsers();

        saveUsers();
        saveOrders();
    }*/

/*    @Test
    public void findOrdersByUserId() {
        String userId = userRepository.findByUsername("pepe").map(User::getId).orElse("");
        if (userId.isEmpty()) throw new AssertionError();
        List<String> orders = orderRepository
                .findByUserId(userId)
                .stream().map(Order::getDescription).toList();
        System.out.println("\nPedidos:\n");
        orders.forEach(System.out::println);
        Assert.assertEquals(2, orders.size());
    }*/
}
