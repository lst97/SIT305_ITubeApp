package com.example.itubeapp.helpers;

public class RepositoryHelper implements RepositoryHelperFactory {

    private final String repositoryName;
    private boolean isLogEnabled = false;


    public RepositoryHelper(String repositoryName) {
        this.repositoryName = repositoryName;
        onCreate();
    }

    public RepositoryHelper(String repositoryName, boolean isLogEnabled) {
        this.repositoryName = repositoryName;
        this.isLogEnabled = isLogEnabled;
        onCreate();
    }

    @Override
    public String getRepositoryName() {
        return repositoryName;
    }

    @Override
    public void onCreate() {

    }

    public boolean isLogEnabled() {
        return isLogEnabled;
    }
}
