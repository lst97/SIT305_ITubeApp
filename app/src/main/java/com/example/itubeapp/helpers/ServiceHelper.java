package com.example.itubeapp.helpers;

import android.content.Context;

// information about the service
public class ServiceHelper implements ServiceHelperFactory {
    private final Context context;
    private final String serviceName;

    private boolean isLogEnabled = false;

    public ServiceHelper(Context context, String name) {
        this.serviceName = name;
        this.context = context;
        onCreate();
    }

    public ServiceHelper(Context context, String name, boolean isLogEnabled) {
        this.serviceName = name;
        this.context = context;
        this.isLogEnabled = isLogEnabled;
        onCreate();
    }

    public boolean isLogEnabled() {
        return isLogEnabled;
    }

    @Override
    public void onCreate() {

    }

    public String getServiceName() {
        return serviceName;
    }

    public Context getContext() {
        return context;
    }
}
