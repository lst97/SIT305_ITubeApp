package com.example.itubeapp.services.authenticate.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.itubeapp.handlers.ServicesHandler;
import com.example.itubeapp.helpers.PreferenceType;
import com.example.itubeapp.helpers.ServiceHelper;
import com.example.itubeapp.models.User;
import com.example.itubeapp.services.ServiceFactory;
import com.example.itubeapp.services.log.LogService;
import com.example.itubeapp.services.log.LogTypes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SessionService implements SessionFactory, ServiceFactory {
    private final SharedPreferences sharedPreferences;
    private final String serviceName;
    private final boolean isLogEnabled;
    private final LogService logService;

    public SessionService(ServiceHelper helper) {
        this.isLogEnabled = false;
        this.logService = null;
        this.serviceName = helper.getServiceName();
        this.sharedPreferences = helper.getContext().getSharedPreferences(PreferenceType.SESSION, Context.MODE_PRIVATE);
        onCreate();
    }

    public SessionService(ServiceHelper helper, boolean isLogEnabled) {
        this.isLogEnabled = isLogEnabled;
        if (isLogEnabled) {
            this.logService = (LogService) ServicesHandler.getInstance().getService(LogService.class.getName());
        } else {
            this.logService = null;
        }

        this.serviceName = helper.getServiceName();
        this.sharedPreferences = helper.getContext().getSharedPreferences(PreferenceType.SESSION, Context.MODE_PRIVATE);
        onCreate();
    }

    private String serializeUser(User user) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    private User deserializeUser(String serializedUser) {
        byte[] bytes = Base64.decode(serializedUser, Base64.DEFAULT);
        User user;

        try {
            user = (User) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    @Override
    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public User getSession() {
        String serializedUser = sharedPreferences.getString(PreferenceType.USER, null);
        if (serializedUser != null) {
            return deserializeUser(serializedUser);
        }
        return null;
    }

    @Override
    public void setSession(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String serializedUser = serializeUser(user);
        editor.putString("user", serializedUser);
        editor.apply();
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void onCreate() {
        if (isLogEnabled) {
            logService.log(serviceName + " Created", LogTypes.INFO);
        }
    }
}
