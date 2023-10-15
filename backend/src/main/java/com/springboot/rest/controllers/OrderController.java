package com.springboot.rest.controllers;

import com.springboot.rest.model.Item;
import com.springboot.rest.model.Order;
import com.springboot.rest.model.User;
import com.springboot.rest.services.OrderService;
import com.springboot.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getOrderList());
    }

    /*@GetMapping("/user/{id}/orders")
    public ResponseEntity<Object> getOrdersByUserId(@PathVariable int id) {
        List<Order> orders = orderService.getOrdersByUserId(id);
        return ResponseEntity.ok().body(orders);
    }*/

    private Map<String, Object> errorDetails(int id) {
        return new HashMap<>() {{
            put("error", "La orden con ID " + id + " no se encontró en el sistema.");
            put("detalle", "Asegúrese de que la URL esté escrita correctamente o pruebe con un ID de recurso diferente.");
            put("timestamp", new Date());
        }};
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        if (order != null) return ResponseEntity.ok().body(order);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Integer.parseInt(id));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<Object> getItemsByOrderById(@PathVariable String id) {
        Order order = orderService.getOrderById(id);
        if (order != null) return ResponseEntity.ok().body(order.getItems());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails(Integer.parseInt(id)));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Object> addOrder(@PathVariable String userId) {

        // Crea la nueva orden
        Order newOrder = orderService.createOrder(userId);

        if (newOrder != null) {
            // Crea una URI que apunta al nuevo recurso.
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/{id}")
                    .buildAndExpand(newOrder.getId())
                    .toUri();

            return ResponseEntity.created(location).body(newOrder);
        } else {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("error", "El usuario con ID " + userId + " no se encontró en el sistema.");
            errorDetails.put("detalle", "Asegúrese de que la URL esté escrita correctamente o pruebe con un ID de recurso diferente.");
            errorDetails.put("timestamp", new Date());

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorDetails);
        }

    }

    @PostMapping("/{id}/{userId}")
    public ResponseEntity<Object> addItem(@PathVariable String id, @PathVariable String userId, @RequestBody Item item) {

        // Crea la nueva orden
        Order order = orderService.addItem(id, userId, item);
        //TODO: Fijarse como hacer para que cuando no existe el user devuelva un status code correcto (en este caso devuelve un 500 pero deberia devolver UNPROCESSABLE_ENTITY o BAD_REQUEST)
        if (order != null) {
            // Crea una URI que apunta al nuevo recurso.
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/orders/{id}/items")
                    .buildAndExpand(order.getId())
                    .toUri();

            return ResponseEntity.created(location).body(order);
        } else {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("error", "La orden con ID " + id + " no se encontró en el sistema o no existe el user con el id " + userId + ".");
            errorDetails.put("detalle", "Asegúrese de que la URL esté escrita correctamente o pruebe con un ID de recurso diferente.");
            errorDetails.put("timestamp", new Date());

            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorDetails);
        }
    }

    @PatchMapping("/{id}/")
    public ResponseEntity<Order> shareOrder(@PathVariable String id, @RequestBody User user) {
        System.out.println("ENTRAA");

        return ResponseEntity.ok().body(orderService.shareOrder(id, user));
    }

    @PatchMapping("/{id}/{userId}")
    public ResponseEntity<Order> modifyOrder(@PathVariable String id, @PathVariable String userId, @RequestBody Order order) {
        if(order.getDescription() != null)
            return ResponseEntity.ok().body(orderService.changeDescription(id, order.getDescription()));
        if(order.isClosed() != null)
            return ResponseEntity.ok().body(orderService.closeOrder(id, userId, order.isClosed()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrderById(id);
    }
    
}
