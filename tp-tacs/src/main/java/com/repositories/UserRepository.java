package com.repositories;

import com.model.User;
import java.util.Optional;

public class UserRepository extends Repository<User> {

    public UserRepository() {
        User pepe = new User("pepe");
        pepe.setId(1);
        User carla = new User("carla");
        carla.setId(2);
        User alex = new User("alex");
        alex.setId(3);

        entities.add(pepe);
        entities.add(carla);
        entities.add(alex);
    }

    @Override
    public Optional<User> findById(int id) {
        return entities.stream().filter(e -> e.getId() == id).findFirst();
    }

    @Override
    public int maxId() {
        return entities.stream().mapToInt(User::getId).max().orElse(0) + 1;
    }

}
