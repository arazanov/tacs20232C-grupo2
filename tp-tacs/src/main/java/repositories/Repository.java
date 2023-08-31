package repositories;

import daos.Dao;
import jakarta.ws.rs.core.Response;

import java.util.List;

public class Repository<T> {
    protected Dao<T> dao;

    public List<T> getAll() {
        return dao.getAll();
    }

    public T get(int id) {
        return dao.get(id).orElse(null);
    }

    public Response create(T t) {
        dao.save(t);
        return Response.ok().build();
    }

    public void update(T t, int id) {
        dao.update(t, id);
    }

    public Response delete(int id) {
        dao.delete(id);
        return Response.ok().build();
    }

}
