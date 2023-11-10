package com.springboot.rest.services;

import com.springboot.rest.model.User;
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

    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(email)).orElseThrow();
    }

    public long userCount() {
        return userRepository.count();
    }

    public boolean exists(String username, String email) {
        return userRepository.existsByUsernameOrEmail(
                username,
                email
        );
    }

    public void updateUser(String id, String username, String email, String password) {
        User update = userRepository.findById(id).orElseThrow();
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
