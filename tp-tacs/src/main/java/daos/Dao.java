package daos;

import java.util.*;

public abstract class Dao<T> {
    protected Map<Integer, T> entities = new HashMap<>();

    public Optional<T> get(int id) {
        return Optional.ofNullable(entities.get(id));
    }

    public List<T> getAll() {
        return new ArrayList<>(entities.values());
    }

    public void save(T t) {
        entities.put(Collections.max(entities.keySet()) + 1, t);
    }

    public void update(T t, int id) {

    }

    public void delete(int id) {
        entities.remove(id);
    }
}
