package com.springboot.rest.services;

import com.springboot.rest.model.User;
import com.springboot.rest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    /*public boolean exists(User user) {
        return userRepository.exists(Example.of(user));
    }*/

    public User updateUserById(User user) {
        Optional<User> userFound = userRepository.findById(user.getId());
        if(userFound.isEmpty()) return null;
        User userUpdate = userFound.get();
        userUpdate.setUsername(user.getUsername());
        return userRepository.updateById(userUpdate);
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
