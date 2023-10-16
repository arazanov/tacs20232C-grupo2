package com.springboot.rest.repositories;

import com.springboot.rest.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
//public interface UserRepository extends MongoRepository<User,String> {
public class UserRepository extends EntityRepository<User> {

    public UserRepository() {
        entities = new ArrayList<>();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String[] names = {"pepe", "carla", "alex", "juan", "maria", "lucas", "ana", "sergio", "laura", "diego"};

        for (int i = 0; i < names.length; i++) {
            User user = new User(
                    names[i],
                    names[i] + "@gmail.com",
                    passwordEncoder.encode("123")
            );
            user.setId(String.valueOf(i + 1));
            entities.add(user);
        }

    }

    @Override
    public String getId(User user) {
        return user.getId();
    }

    public Optional<User> findByUsername(String username) {
        return entities.stream().filter(e -> e.getUsername().equals(username)).findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return entities.stream().filter(e -> e.getEmail() != null && e.getEmail().equals(email)).findFirst();
    }
}
