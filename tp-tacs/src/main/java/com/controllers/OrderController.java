package com.controllers;

import com.model.Item;
import com.model.Order;
import com.model.User;
import com.services.OrderService;
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
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getOrderList());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable int id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return ResponseEntity.ok().body(order);
        } else {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("error", "La orden con ID " + id + " no se encontró en el sistema.");
            errorDetails.put("detalle", "Asegúrese de que la URL esté escrita correctamente o pruebe con un ID de recurso diferente.");
            errorDetails.put("timestamp", new Date());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
        }

        /*return ResponseEntity.ofNullable(orderService.getOrderById(id));*/
    }

    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<Object> getItemsByOrderById(@PathVariable int orderId) {

        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok().body(order.getItems());
        } else {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("error", "La orden con ID " + orderId + " no se encontró en el sistema.");
            errorDetails.put("detalle", "Asegúrese de que la URL esté escrita correctamente o pruebe con un ID de recurso diferente.");
            errorDetails.put("timestamp", new Date());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
        }
    }

    @PostMapping("/orders/{userId}")
    public ResponseEntity<Object> addOrder(@PathVariable int userId) {

        // Crea la nueva orden
        Order newOrder = orderService.createOrder(userId);

        if (newOrder != null) {
            // Crea una URI que apunta al nuevo recurso.
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .replacePath("/orders/{id}")
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

    @PostMapping("/orders/{id}/{userId}")
    public ResponseEntity<Object> addItem(@PathVariable int id, @PathVariable int userId, @RequestBody Item item) {

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

    @PatchMapping("/orders/{id}/")
    public ResponseEntity<Order> shareOrder(@PathVariable int id, @RequestBody User user) {
        return ResponseEntity.ok().body(orderService.shareOrder(id, user));
    }

    @PatchMapping("/orders/{id}/{userId}")
    public ResponseEntity<Order> closeOrder(@PathVariable int id, @PathVariable int userId, @RequestBody Order order) {
        return ResponseEntity.ok().body(orderService.closeOrder(id, userId, order.isClosed()));
    }

    @DeleteMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable int id) {
        orderService.deleteOrderById(id);
    }
    
}
