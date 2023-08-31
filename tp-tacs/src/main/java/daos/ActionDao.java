package daos;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import model.Action;

import java.util.ArrayList;

@Path("action")
@Produces("text/xml")
public class ActionDao extends Dao<Action> {

    public ActionDao() {
        elements = new ArrayList<>();
    }

    @Override
    public void update(Action action, String[] params) {

    }
}
