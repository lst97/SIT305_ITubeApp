package com.example.itubeapp.services.authenticate.session;

import com.example.itubeapp.models.User;

public interface SessionFactory {
    void clearSession();

    User getSession();

    void setSession(User user);
}
