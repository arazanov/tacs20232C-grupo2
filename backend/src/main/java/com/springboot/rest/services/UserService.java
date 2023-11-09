package com.springboot.rest.services;

import com.springboot.rest.model.User;
import com.springboot.rest.payload.SignUpRequest;
import com.springboot.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsername(username).orElseGet(() ->
                userRepository.findByEmail(email).orElseThrow()
        );
    }

    public long userCount() {
        return userRepository.count();
    }

    public boolean exists(SignUpRequest request) {
        return userRepository.existsByUsernameOrEmail(
                request.getUsername(),
                request.getEmail()
        );
    }

    public void updateUser(String id, String username, String email, String password) {
        User update = findById(id);
        update.setUsername(username);
        update.setEmail(email);
        if (password != null) update.setPassword(password);
        userRepository.deleteById(id);
        userRepository.save(update);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

}
