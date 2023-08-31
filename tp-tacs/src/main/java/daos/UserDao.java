package daos;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import model.User;

import java.util.ArrayList;
import java.util.Objects;

@Path("user")
@Produces("text/xml")
public class UserDao extends Dao<User> {

    public UserDao() {
        elements = new ArrayList<>();
        elements.add(new User("pepe"));
        elements.add(new User("carla"));
        elements.add(new User("alex"));
    }

    @Override
    public void update(User user, String[] params) {
        user.setUsername(Objects.requireNonNull(
                params[0], "Name cannot be null"));

        elements.add(user);
    }
}
