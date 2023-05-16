package com.example.itubeapp.handlers;

import com.example.itubeapp.helpers.RepositoryHelper;
import com.example.itubeapp.persistent.RepositoryFactory;
import com.example.itubeapp.persistent.UserRepository;
import com.example.itubeapp.services.log.LogService;
import com.example.itubeapp.services.log.LogTypes;

import java.util.ArrayList;
import java.util.List;

public class RepositoriesHandler implements RepositoriesHandlerFactory<RepositoryFactory> {

    private static RepositoriesHandler instance;
    private final LogService logService;
    List<Object> repositories;

    RepositoriesHandler(RepositoryHelper helper) {
        if (helper.isLogEnabled()) {
            this.logService = (LogService) ServicesHandler.getInstance().getService(LogService.class.getName());
        } else {
            this.logService = null;
        }

        onCreate();
    }

    public static RepositoriesHandler getInstance(RepositoryHelper helper) {
        if (instance == null) {
            instance = new RepositoriesHandler(helper);
        }
        return instance;
    }

    public static RepositoriesHandler getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        repositories = new ArrayList<>();

        if (logService != null) {
            logService.log(RepositoriesHandler.class.getName() + " Created", LogTypes.INFO);
        }
    }

    @Override
    public void addRepository(RepositoryFactory repository) {
        // check if service already exists
        for (Object r : repositories) {
            if (r instanceof UserRepository) {
                if (((UserRepository) r).getRepositoryName().equals(repository.getRepositoryName())) {
                    if (logService != null)
                        logService.log("Repository " + repository.getRepositoryName() + " already exist.", LogTypes.INFO);
                    return;
                }
            }
        }
        repositories.add(repository);
    }

    @Override
    public void removeRepository(RepositoryFactory repository) {
        repositories.remove(repository);
    }

    /**
     * It is the caller's responsibility to cast the returned object to the correct type
     *
     * @param repositoryName The name of the repository to be returned
     * @return The repository with the given name
     */
    @Override
    public RepositoryFactory getRepository(String repositoryName) {
        for (Object repository : repositories) {
            if (repository instanceof RepositoryFactory) {
                if (((RepositoryFactory<?>) repository).getRepositoryName().equals(repositoryName)) {
                    return (RepositoryFactory) repository;
                }
            }
        }
        return null;
    }
}
