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

    public Response update(int id, T t) {
        T optionalT = get(id);
        if(optionalT == null) return Response.status(Response.Status.NOT_FOUND).build();
        if(optionalT.equals(t)) return Response.notModified().build();
        dao.update(id, t);
        return Response.ok().build();
    }

    public Response delete(int id) {
        T optionalT = get(id);
        if(optionalT == null) return Response.status(Response.Status.NOT_FOUND).build();
        dao.delete(id);
        return Response.ok().build();
    }

}
