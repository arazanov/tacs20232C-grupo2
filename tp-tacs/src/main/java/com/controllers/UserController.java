package com.controllers;


import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.services.UserService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            System.out.println(user.isNeverInteracted());
            return ResponseEntity.ok().body(user);
        } else {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("error", "El usuario con ID " + id + " no se encontró en el sistema.");
            errorDetails.put("detalle", "Asegúrese de que la URL esté escrita correctamente o pruebe con un ID de recurso diferente.");
            errorDetails.put("timestamp", new Date());

            // Devolver un ResponseEntity con el código de estado 404 y el objeto JSON personalizado en el cuerpo
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        // Crea el nuevo usuario
        User newUser = userService.createUser(user);

        // Crea una URI que apunta al nuevo recurso.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(newUser);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.updateUserById(user));
    }

    @DeleteMapping("/users/{id}")
    public HttpStatus deleteUser(@PathVariable int id) {
        userService.deleteUserById(id);
        return HttpStatus.NO_CONTENT;
    }

}
