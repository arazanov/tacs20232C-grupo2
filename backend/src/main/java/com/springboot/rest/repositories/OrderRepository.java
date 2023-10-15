package com.springboot.rest.repositories;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface OrderRepository extends MongoRepository<Order,String> {

   /* public OrderRepository() {
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
    public String getId(Order order) {
        return order.getId();
    }
*/
    @Query("{ 'user.id' : ?0 }")
    Optional<List<Order>> findByUserId(String id);

}
