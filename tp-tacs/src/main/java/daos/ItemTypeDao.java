package daos;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import model.ItemType;

import java.util.ArrayList;

@Path("item-type")
@Produces("text/xml")
public class ItemTypeDao extends Dao<ItemType> {

    public ItemTypeDao() {
        elements = new ArrayList<>();
        elements.add(new ItemType("empanada de carne", "empanada de carne cortada a cuchillo con cebolla", 200));
        elements.add(new ItemType("empanada de verdura", "empanada de acelga con ricota", 200));
        elements.add(new ItemType("pizza napolitana", "muzzarella, tomates en rodajas, ajo", 1200));
        elements.add(new ItemType("pizza fugazzeta", "muzzarella y cebolla caramelizada", 1100));
    }

    @Override
    public void update(ItemType itemType, String[] params) {

    }
}
