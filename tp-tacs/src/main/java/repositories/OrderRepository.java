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
    @Override
    public Response create(Order order) {
        return super.create(order);
    }

    @PATCH
    @Path("{id}/close")
    public Response close(@PathParam("id") int id, User user) {
        Order order = get(id);
        if(order == null) return Response.status(Response.Status.NOT_FOUND).build();
        order.close(user);
        dao.update(id, order);
        return Response.ok().build();
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@PathParam("id") int id) {
        return super.delete(id);
    }

}
