package com.springboot.rest.repositories;

import com.springboot.rest.model.User;

import java.util.Optional;

public class UserRepository extends Repository<User> {

    public UserRepository() {

        String[] names = {"pepe", "carla", "alex", "juan", "maria", "lucas", "ana", "sergio", "laura", "diego"};

        for (int i = 0; i < names.length; i++) {
            User user = new User(names[i]);
            user.setId(i + 1);
            user.setPassword("123");
            entities.add(user);
        }

    }

    @Override
    public int getId(User user) {
        return user.getId();
    }

    public Optional<User> findByUsername(String username) {
        return entities.stream().filter(e -> e.getUsername().equals(username)).findFirst();
    }

}
