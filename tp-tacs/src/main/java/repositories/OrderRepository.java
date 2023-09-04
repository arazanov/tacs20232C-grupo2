package repositories;

import daos.OrderDao;
import model.Order;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.User;

import java.util.List;

@Path("orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderRepository extends Repository<Order> {

    public OrderRepository() {
        dao = new OrderDao();
    }

    @GET
    @Override
    public List<Order> getAll() {
        return super.getAll();
    }

    @GET
    @Path("{id}")
    @Override
    public Order get(@PathParam("id") int id) {
        return super.get(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(User user) {
        return super.create(new Order(user));
    }

    @PUT
    @Path("{id}")
    @Override
    public Response update(@PathParam("id") int id, Order order) {
        return super.update(id, order);
    }

    @Path("{id}")
    public Order modify(@PathParam("id") int id) {
        return get(id);
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@PathParam("id") int id) {
        return super.delete(id);
    }

}
