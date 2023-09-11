package com.services;

import com.model.User;
import com.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.repositories.Repository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final Repository<User> userRepository = new UserRepository();

    public User createUser(User user) {
        int id = userRepository.maxId();
        user.setId(id);
        return userRepository.save(user);
    }

    /*public List<User> createUserList(List<User> list) {
        return userRepository.saveAll(list);
    }*/

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUserById(User user) {
        Optional<User> userFound = userRepository.findById(user.getId());

        if (userFound.isPresent()) {
            User userUpdate = userFound.get();
            userUpdate.setUsername(user.getUsername());
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

}
