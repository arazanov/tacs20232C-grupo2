package com.springboot.rest.controllers;

import com.springboot.rest.model.User;
import com.springboot.rest.payload.JwtResponse;
import com.springboot.rest.payload.LoginRequest;
import com.springboot.rest.payload.SignUpRequest;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    private JwtResponse authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new JwtResponse(jwtUtils.generateJwtToken(userDetails.getUsername()));
    }

    @PostMapping("/")
    public JwtResponse userLogin(@RequestBody LoginRequest loginRequest) {
        return authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
    }

    @PostMapping("/users")
    public ResponseEntity<?> userSignUp(@RequestBody SignUpRequest signUpRequest) {
        if (userService.exists(signUpRequest)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, //409
                    "User already exists");
        }

        User user = userService.save(new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()))
        );

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(authenticateUser(
                signUpRequest.getUsername(),
                signUpRequest.getPassword()
        ));
    }

    private String userId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public User getUserById() {
        String id = userId();
        try {
            return userService.findById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El usuario con ID " + id + " no se encontró en el sistema");
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public User getUserByUsernameOrEmail(@RequestParam String username, @RequestParam String email) {
        try {
            return userService.findByUsernameOrEmail(username, email);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "El usuario " + (username!=null ? username : email) + " no se encontró en el sistema");
        }
    }

    @PutMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public JwtResponse updateUser(@RequestBody SignUpRequest user) {
        userService.updateUser(
                userId(),
                user.getUsername(),
                user.getEmail(),
                passwordEncoder.encode(user.getPassword())
        );
        return authenticateUser(user.getUsername(), user.getPassword());
    }

    @DeleteMapping("/users")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser() {
        String id = userId();
        userService.deleteById(id);
        orderService.deleteByUserId(id);
    }

}
