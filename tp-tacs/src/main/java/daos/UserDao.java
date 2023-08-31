package daos;

import model.User;

public class UserDao extends Dao<User> {

    public UserDao() {
        elements.put(1, new User("pepe"));
        elements.put(2, new User("carla"));
        elements.put(3, new User("alex"));
    }

}
