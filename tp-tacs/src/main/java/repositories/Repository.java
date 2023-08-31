package repositories;

import daos.Dao;

import java.util.List;

public class Repository<T> {

    public Repository(Dao<T> dao) {
        this.dao = dao;
    }

    private Dao<T> dao;

    public List<T> read() {
        return dao.getAll();
    }

    public T readById(int id) {
        return dao.get(id).orElse(null);
    }

    public void create(T t) {
        dao.save(t);
    }

    public void update(T t, String[] params) {
        dao.update(t, params);
    }

    public void delete(T t) {
        dao.delete(t);
    }

}
