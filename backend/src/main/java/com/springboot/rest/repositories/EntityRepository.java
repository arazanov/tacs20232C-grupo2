package com.springboot.rest.repositories;

import java.util.List;
import java.util.Optional;

public abstract class EntityRepository<T> {
    protected List<T> entities;

    public abstract String getId(T t);

    public List<T> findAll() {
        return entities;
    }

    public Optional<T> findById(String id) {
        return entities.stream().filter(e -> getId(e).equals(id)).findFirst();
    }

    public T updateById(T t) {
        findById(getId(t)).ifPresent(e -> deleteById(getId(e)));
        return save(t);
    }

    public String maxId() {
        int maxId = entities.stream().mapToInt(e -> Integer.parseInt(getId(e))).max().orElse(0) + 1;
        return String.valueOf(maxId);
    }

    public T save(T t) {
        entities.add(t);
        return t;
    }

    public void deleteById(String id) {
        Optional<T> optionalT = findById(id);
        optionalT.ifPresent(t -> entities.remove(t));
    }
}
