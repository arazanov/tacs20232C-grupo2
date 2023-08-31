package repositories;

import daos.ItemTypeDao;
import model.ItemType;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("itemTypes")
@Produces(MediaType.APPLICATION_JSON)
public class ItemTypeRepository extends Repository<ItemType> {

    public ItemTypeRepository() {
        dao = new ItemTypeDao();
    }

    @GET
    @Override
    public List<ItemType> getAll() {
        return super.getAll();
    }

    @GET
    @Path("{id}")
    @Override
    public ItemType get(@PathParam("id") int id) {
        return super.get(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(ItemType itemType) {
        return super.create(itemType);
    }

    @Override
    public void update(ItemType itemType, int id) {
        super.update(itemType, id);
    }

    @DELETE
    @Path("{id}")
    @Override
    public Response delete(@PathParam("id") int id) {
        return super.delete(id);
    }

}
