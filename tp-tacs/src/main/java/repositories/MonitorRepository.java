package repositories;

import model.Monitor;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("monitor")
@Produces(MediaType.APPLICATION_JSON)
public class MonitorRepository {

    @GET
    public Monitor get() {
        return Monitor.getInstance();
    }

}
