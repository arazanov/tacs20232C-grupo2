package com.springboot.rest.services;

import com.springboot.rest.model.User;
import com.springboot.rest.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public User createUser(User user) {
        int id = userRepository.maxId();
        user.setId(id);
        return userRepository.save(user);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean exists(User user) {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        return optionalUser.map(u -> u.getPassword().equals(user.getPassword())).orElse(false);
    }

    public User updateUserById(User user) {
        Optional<User> userFound = userRepository.findById(user.getId());
        if(userFound.isEmpty()) return null;
        User userUpdate = userFound.get();
        userUpdate.setUsername(user.getUsername());
        return userRepository.updateById(userUpdate);
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

}
