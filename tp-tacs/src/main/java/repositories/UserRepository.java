package repositories;

import daos.Dao;
import daos.UserDao;
import model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("users")
@Produces("text/xml")
public class UserRepository implements Repository<User> {
    private final Dao<User> dao = new UserDao();

    @GET
    @Override
    public List<User> getAll() {
        return dao.getAll();
    }

    @GET
    @Path("{id}")
    @Override
    public User get(@PathParam("id") int id) {
        return dao.get(id).orElse(null);
    }

    @POST
    @Override
    public Response create(User user) {
        dao.save(user);
        return Response.ok().build();
    }

    @Override
    public void update(User user, int id) {
        dao.update(user, id);
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@PathParam("id") int id) {
        dao.delete(id);
        return Response.ok().build();
    }
}
