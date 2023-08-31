package repositories;

import javax.ws.rs.core.Response;
import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    T get(int id);
    Response create(T t);
    void update(T t, int id);
    Response delete(int id);
}
