package repositories;

import daos.OrderDao;
import model.Order;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

    @Override
    public void update(Order order, int id) {
        super.update(order, id);
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@PathParam("id") int id) {
        return super.delete(id);
    }

}
