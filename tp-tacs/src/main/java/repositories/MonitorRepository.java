package repositories;

import model.Monitor;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("monitor")
@Produces("text/xml")
public class MonitorRepository {

    @GET
    public Monitor get() {
        return Monitor.getInstance();
    }

}
