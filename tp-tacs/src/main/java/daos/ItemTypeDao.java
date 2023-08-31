package daos;

import model.ItemType;

public class ItemTypeDao extends Dao<ItemType> {

    public ItemTypeDao() {
        elements.put(1, new ItemType(1, "empanada de carne", "empanada de carne cortada a cuchillo con cebolla", 200));
        elements.put(2, new ItemType(2, "empanada de verdura", "empanada de acelga con ricota", 200));
        elements.put(3, new ItemType(3, "pizza napolitana", "muzzarella, tomates en rodajas, ajo", 1200));
        elements.put(4, new ItemType(4, "pizza fugazzeta", "muzzarella y cebolla caramelizada", 1100));
    }

}
