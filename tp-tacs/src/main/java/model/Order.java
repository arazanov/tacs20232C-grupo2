package model;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.*;
import java.util.stream.Collectors;

@XmlRootElement(name = "Order")
public class Order {

    public Order() {
    }

    public Order(User user) {
        this.user = user;
        this.users = new HashSet<>();
        this.items = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.closed = false;

        this.actions.add(new Action(user, " created an order"));
        Monitor.getInstance().orderCreated(user);
    }

    private User user;
    private Set<User> users;
    private List<Item> items;
    private List<Action> actions;
    private boolean closed;

    @POST
    @Path("share")
    public Response shareWith(List<User> users) {
        this.users.addAll(users);
        actions.add(new Action(user, " shared the order with " + users.stream().map(User::getUsername).collect(Collectors.joining(","))));
        return Response.ok().build();
    }

    @POST
    @Path("addItems")
    public Response addItems(User user, @QueryParam("description") String description, @QueryParam("quantity") int quantity) {
        if(unauthorized(user)) return Response.status(Response.Status.FORBIDDEN).build();
        Monitor.getInstance().userInteraction(user);

        Optional<Item> itemOptional = find(description);

        if (itemOptional.isPresent()) itemOptional.get().addItems(quantity);
        else {
            Item item = new Item(description);
            item.addItems(quantity);
            items.add(item);
        }

        actions.add(new Action(user, " added " + quantity + " \"" + description + "\""));
        return Response.ok().build();
    }

    @POST
    @Path("removeItems")
    public Response removeItems(User user, @QueryParam("description") String description, @QueryParam("quantity") int quantity) {
        if(unauthorized(user)) return Response.status(Response.Status.FORBIDDEN).build();
        Monitor.getInstance().userInteraction(user);

        find(description).ifPresent(i -> {
            i.removeItems(quantity);
            actions.add(new Action(user, " removed " + quantity + " \"" + description + "\""));
        });

        return Response.ok().build();
    }

    @PATCH
    @Path("close")
    public Response close(User user) {
        if(isClosed()) return Response.status(Response.Status.NOT_MODIFIED).build();
        if(this.user.getUsername().equals(user.getUsername())) {
            this.closed = true;
            actions.add(new Action(user, " closed the order"));
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @PATCH
    @Path("reopen")
    public Response reopen(User user) {
        if(!isClosed()) return Response.status(Response.Status.NOT_MODIFIED).build();
        if(this.user.getUsername().equals(user.getUsername())) {
            this.closed = false;
            actions.add(new Action(user, "reopen the order"));
            return Response.ok().build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    private Optional<Item> find(String description) {
        return items.stream().filter(i -> i.getDescription().equals(description)).findFirst();
    }

    private boolean unauthorized(User user) {
        return isClosed() ||
                !user.getUsername().equals(this.user.getUsername()) &&
                        users.stream().noneMatch(u -> u.getUsername().equals(user.getUsername()));
    }

    public User getUser() {
        return user;
    }

    public Set<User> getUsers() {
        return users;
    }

    @GET
    @Path("items")
    public List<Item> getItems() {
        return items;
    }

    public List<Action> getActions() {
        return actions;
    }

    public boolean isClosed() {
        return closed;
    }

}