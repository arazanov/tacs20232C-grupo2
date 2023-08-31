package repositories;

import daos.UserDao;
import model.User;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UserRepository extends Repository<User> {

    public UserRepository() {
        dao = new UserDao();
    }

    @GET
    @Override
    public List<User> getAll() {
        return super.getAll();
    }

    @GET
    @Path("{id}")
    @Override
    public User get(@PathParam("id") int id) {
        return super.get(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(User user) {
        return super.create(user);
    }

    @Override
    public void update(User user, int id) {
        super.update(user, id);
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@PathParam("id") int id) {
        return super.delete(id);
    }

}
