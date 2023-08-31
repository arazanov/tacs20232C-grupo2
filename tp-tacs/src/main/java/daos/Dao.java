package daos;

import java.util.List;
import java.util.Optional;

public abstract class Dao<T> {
    protected List<T> elements;

    public Optional<T> get(int id) {
        return Optional.ofNullable(elements.get(id));
    }

    public List<T> getAll() {
        return elements;
    }

    public void save(T t) {
        elements.add(t);
    }

    public abstract void update(T t, String[] params);

    public void delete(T t) {
        elements.remove(t);
    }
}
