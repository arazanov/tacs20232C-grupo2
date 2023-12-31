package com.springboot.rest.controllers;

import com.springboot.rest.model.User;
import com.springboot.rest.security.jwt.JwtUtils;
import com.springboot.rest.security.services.CustomUserDetails;
import com.springboot.rest.services.ItemService;
import com.springboot.rest.services.OrderService;
import com.springboot.rest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private ItemService itemService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/")
    public JwtResponse userLogin(@RequestBody UserRequest request) {
        return authenticateUser(request.username(), request.password());
    }

    @PostMapping("/users")
    public ResponseEntity<JwtResponse> userSignUp(@RequestBody UserRequest request) {
        if (userService.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode((request.password())));
        user = userService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(authenticateUser(
                request.username(),
                request.password()
        ));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails.getUser();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public User getUserByUsernameOrEmail(@RequestParam String username) {
        return findOrThrow(
                () -> userService.findByUsernameOrEmail(username),
                username
        );
    }

    @PutMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public JwtResponse updateUser(@RequestBody UserRequest request,
                                  @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User update = userDetails.getUser();

        String newUsername = request.username();
        String newEmail = request.email();

        if (!newUsername.equals(userDetails.getUsername()) && userService.existsByUsername(newUsername) ||
                !newEmail.equals(userDetails.getEmail()) && userService.existsByEmail(newEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        String newPassword = request.password();

        update.setUsername(newUsername);
        update.setEmail(newEmail);
        if (newPassword != null)
            update.setPassword(passwordEncoder.encode(newPassword));

        userService.save(update);
        return new JwtResponse(jwtUtils.generateJwtToken(newUsername));
    }

    @DeleteMapping("/users")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String id = userDetails.id();
        userService.deleteById(id);
        orderService.findByUserId(id).forEach(o -> {
            itemService.deleteByOrderId(o.getId());
            o.removeUser(id);
        });
        orderService.deleteByUserId(id);
    }

    public record JwtResponse(String token) {
    }

    public record UserRequest(String username, String email, String password) {
    }

    private JwtResponse authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return new JwtResponse(jwtUtils.generateJwtToken(userDetails.getUsername()));
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
