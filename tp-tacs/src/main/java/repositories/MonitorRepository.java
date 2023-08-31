package repositories;

import model.Monitor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("monitor")
@Produces({MediaType.APPLICATION_JSON})
public class MonitorRepository {

    @GET
    public Monitor get() {
        return Monitor.getInstance();
    }

}
