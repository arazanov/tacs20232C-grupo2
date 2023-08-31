package daos;

import java.util.*;

public abstract class Dao<T> {
    protected Map<Integer, T> elements = new HashMap<>();

    public Optional<T> get(int id) {
        return Optional.ofNullable(elements.get(id));
    }

    public List<T> getAll() {
        return new ArrayList<>(elements.values());
    }

    public void save(T t) {
        Integer maxKey = Collections.max(elements.keySet());
        elements.put(maxKey + 1, t);
    }

    public void update(T t, int id) {

    }

    public void delete(int id) {
        elements.remove(id);
    }
}
