package repositories;

import daos.Dao;
import daos.ItemTypeDao;
import model.ItemType;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("itemTypes")
@Produces("text/xml")
public class ItemTypeRepository implements Repository<ItemType> {
    private final Dao<ItemType> dao = new ItemTypeDao();

    @GET
    @Override
    public List<ItemType> getAll() {
        return dao.getAll();
    }

    @GET
    @Path("{id}")
    @Override
    public ItemType get(@PathParam("id") int id) {
        return dao.get(id).orElse(null);
    }

    @POST
    @Override
    public Response create(ItemType itemType) {
        dao.save(itemType);
        return Response.ok().build();
    }

    @Override
    public void update(ItemType itemType, int id) {
        dao.update(itemType, id);
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@PathParam("id") int id) {
        dao.delete(id);
        return Response.ok().build();
    }

}
