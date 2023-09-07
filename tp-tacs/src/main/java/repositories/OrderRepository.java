package repositories;

import daos.OrderDao;
import daos.UserDao;
import model.Item;
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
        userDao = new UserDao();
    }

    private final UserDao userDao;

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

//    Como usuario quiero poder ver los items y cantidades que hay en un pedido.

    @GET
    @Path("{id}/items")
    public List<Item> getItems(@PathParam("id") int id) {
        return get(id).getItems();
    }

    @PUT
    @Path("{id}")
    @Override
    public Response update(@PathParam("id") int id, Order order) {
        return super.update(id, order);
    }

//    Como usuario quiero crear un pedido.

    @POST
    public Response create(User user) {
        return super.create(new Order(user));
    }

    @POST
    @Path("{id}/share")
    public Response shareWith(@PathParam("id") int id, User user) {
        get(id).shareWith(user);
        return Response.ok().build();
    }

    public interface Modifiable {
        void apply(Order order, User user, Item item);
    }

    public Response modifyItems(int id, int userId, Item item, Modifiable modifiable) {
        Order order = get(id);
        User user = userDao.get(userId).orElse(null);

        assert user != null;
        if (order.isClosed() || order.isNotTheCreator(user) && order.hasNoPermission(user))
            return Response.status(Response.Status.FORBIDDEN).build();

        modifiable.apply(order, user, item);
        return Response.ok().build();
    }

//    Como usuario quiero agregar un nuevo item a un pedido ya creado.
//    Como usuario quiero poder sumar N elementos (+1 por ejemplo) a un item de un pedido.

    @POST
    @Path("{id}/addItems/{userId}")
    public Response addItems(@PathParam("id") int id, @PathParam("userId") int userId, Item item) {
        return modifyItems(id, userId, item, Order::addItems);
    }

    @POST
    @Path("{id}/removeItems/{userId}")
    public Response removeItems(@PathParam("id") int id, @PathParam("userId") int userId, Item item) {
        return modifyItems(id, userId, item, Order::removeItems);
    }

    public interface Closable {
        void apply(Order order, User user);
    }

    public Response openClose(Order order, int userId, Closable closable) {
        User user = userDao.get(userId).orElse(null);
        assert user != null;
        if (order.isNotTheCreator(user))
            return Response.status(Response.Status.FORBIDDEN).build();

        closable.apply(order, user);
        return Response.ok().build();
    }

//    Como usuario quiero poder cerrar el pedido. Siempre y cuando haya sido creado por mí.

    @PATCH
    @Path("{id}/close/{userId}")
    public Response close(@PathParam("id") int id, @PathParam("userId") int userId) {
        Order order = get(id);
        if (order.isClosed())
            return Response.status(Response.Status.NOT_MODIFIED).build();
        return openClose(order, userId, Order::close);
    }

    @PATCH
    @Path("{id}/reopen/{userId}")
    public Response reopen(@PathParam("id") int id, @PathParam("userId") int userId) {
        Order order = get(id);
        if (!order.isClosed())
            return Response.status(Response.Status.NOT_MODIFIED).build();
        return openClose(order, userId, Order::reopen);
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@PathParam("id") int id) {
        return super.delete(id);
    }

}
