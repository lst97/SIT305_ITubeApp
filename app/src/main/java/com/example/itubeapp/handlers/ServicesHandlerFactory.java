package com.example.itubeapp.handlers;

import com.example.itubeapp.services.ServiceFactory;

public interface ServicesHandlerFactory {
    void onCreate();

    void addService(ServiceFactory service);

    void removeService(ServiceFactory service);

    ServiceFactory getService(String serviceName);
}
