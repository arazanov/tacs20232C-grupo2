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

    public User findByUsernameOrEmail(String username) {
        return userRepository.findByUsernameOrEmail(username).orElseThrow();
    }

    public long userCount() {
        return userRepository.count();
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void updateUser(User user) {
        userRepository.deleteById(user.getId());
        userRepository.save(user);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

}
