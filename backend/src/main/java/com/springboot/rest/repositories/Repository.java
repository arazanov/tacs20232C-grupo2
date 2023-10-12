package com.springboot.rest.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Repository<T> {
    protected List<T> entities = new ArrayList<>();

    public abstract int getId(T t);

    public List<T> findAll() {
        return entities;
    }

    public Optional<T> findById(int id) {
        return entities.stream().filter(e -> getId(e) == id).findFirst();
    }

    public T updateById(T t) {
        findById(getId(t)).ifPresent(e -> deleteById(getId(e)));
        return save(t);
    }

    public int maxId() {
        return entities.stream().mapToInt(this::getId).max().orElse(0) + 1;
    }

    public T save(T t) {
        entities.add(t);
        return t;
    }

    public void deleteById(int id) {
        Optional<T> optionalT = findById(id);
        optionalT.ifPresent(t -> entities.remove(t));
    }
}
