package com.springboot.rest.controllers;

import com.springboot.rest.model.User;
import com.springboot.rest.payload.UserRequest;
import com.springboot.rest.security.jwt.JwtUtils;
import com.springboot.rest.security.services.UserDetailsImpl;
import com.springboot.rest.services.OrderService;
import com.springboot.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/")
    public JwtResponse userLogin(@RequestBody UserRequest request) {
        return authenticateUser(
                request.getUsername(),
                request.getPassword()
        );
    }

    @PostMapping("/users")
    public ResponseEntity<JwtResponse> userSignUp(@RequestBody UserRequest request) {
        if (userService.exists(request.getUsername(), request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, //409
                    "User already exists");
        }

        User user = userService.save(new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()))
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(authenticateUser(
                request.getUsername(),
                request.getPassword()
        ));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public User getUserById() {
        String id = userId();
        return findOrThrow(() -> userService.findById(id), id);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public User getUserByUsernameOrEmail(@RequestParam String username, @RequestParam String email) {
        return findOrThrow(
                () -> userService.findByUsernameOrEmail(username, email),
                (username != null ? username : email)
        );
    }

    @PutMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public JwtResponse updateUser(@RequestBody UserRequest request) {
        userService.updateUser(
                userId(),
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword())
        );
        return authenticateUser(request.getUsername(), request.getPassword());
    }

    @DeleteMapping("/users")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        String id = userId();
        userService.deleteById(id);
        orderService.deleteByUserId(id);
        // TODO borrar items por pedido borrado
    }

    public record JwtResponse(String token) {
    }

    private JwtResponse authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new JwtResponse(jwtUtils.generateJwtToken(userDetails.username()));
    }

    private String userId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userDetails.id();
    }

    private interface Finder<T> {
        T find();
    }

    private User findOrThrow(Finder<User> finder, String queryParam) {
        try {
            return finder.find();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El usuario " + queryParam + " no se encontró en el sistema");
        }
    }

}
