package com.springboot.rest.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Repository<T> {
    protected List<T> entities = new ArrayList<>();

    public List<T> findAll() {
        return entities;
    }

    public abstract Optional<T> findById(int id);

    public abstract int maxId();

    public T save(T t) {
        entities.add(t);
        return t;
    }

    public void deleteById(int id) {
        Optional<T> optionalT = findById(id);
        optionalT.ifPresent(t -> entities.remove(t));
    }
}
