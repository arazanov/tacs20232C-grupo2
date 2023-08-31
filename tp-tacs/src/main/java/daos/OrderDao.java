package daos;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import model.Order;

import java.util.ArrayList;

@Path("order")
@Produces("text/xml")
public class OrderDao extends Dao<Order> {

    public OrderDao() {
        elements = new ArrayList<>();
    }

    @Override
    public void update(Order order, String[] params) {

    }
}
