package repositories;

import daos.Dao;
import daos.OrderDao;
import model.Order;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("orders")
@Produces("text/xml")
public class OrderRepository implements Repository<Order> {
    private final Dao<Order> dao = new OrderDao();

    @GET
    @Override
    public List<Order> getAll() {
        return dao.getAll();
    }

    @GET
    @Path("{id}")
    @Override
    public Order get(@PathParam("id") int id) {
        return dao.get(id).orElse(null);
    }

    @Path("{id}")
    public Order pathToActions(@PathParam("id") int id) {
        return dao.get(id).orElse(null);
    }

    @Path("{id}")
    public Order pathToItems(@PathParam("id") int id) {
        return dao.get(id).orElse(null);
    }

    @POST
    @Override
    public Response create(Order order) {
        dao.save(order);
        return Response.ok().build();
    }

    @Override
    public void update(Order order, int id) {
        dao.update(order, id);
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@PathParam("id") int id) {
        dao.delete(id);
        return Response.ok().build();
    }
}
