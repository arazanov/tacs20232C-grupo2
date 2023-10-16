package com.springboot.rest.controllers;

import com.springboot.rest.model.User;
import com.springboot.rest.payload.ErrorMessage;
import com.springboot.rest.payload.SignUpRequest;
import com.springboot.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUserList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) return ResponseEntity.ok().body(user);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("usuario", id));
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getUsername()))
            return ResponseEntity.badRequest().build();

        if (userService.existsByEmail(signUpRequest.getEmail()))
            return ResponseEntity.badRequest().build();

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(userService.saveUser(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
        return HttpStatus.NO_CONTENT;
    }

}
