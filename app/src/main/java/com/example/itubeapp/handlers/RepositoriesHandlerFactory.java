package com.example.itubeapp.handlers;

public interface RepositoriesHandlerFactory<T> {
    void onCreate();

    void addRepository(T repository);

    void removeRepository(T repository);

    T getRepository(String repositoryName);
}
