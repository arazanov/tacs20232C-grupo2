package daos;

import model.User;

public class UserDao extends Dao<User> {

    public UserDao() {
        entities.put(1, new User("pepe"));
        entities.put(2, new User("carla"));
        entities.put(3, new User("alex"));
    }

}
