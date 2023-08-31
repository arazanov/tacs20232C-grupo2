package daos;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import model.Item;

import java.util.ArrayList;

@Path("item")
@Produces("text/xml")
public class ItemDao extends Dao<Item> {

    public ItemDao() {
        elements = new ArrayList<>();
    }

    @Override
    public void update(Item item, String[] params) {

    }
}
