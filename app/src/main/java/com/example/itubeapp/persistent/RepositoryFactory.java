package com.example.itubeapp.persistent;

import java.util.List;

public interface RepositoryFactory<T> {
    void create(T object);

    void delete(T object);

    void update(T object);

    List<T> readAll();

    T read(int id);

    T read(String name);

    String getRepositoryName();

    void onCreate();

    void drop();
}
