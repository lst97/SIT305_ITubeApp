package com.example.itubeapp.services.authenticate;

import com.example.itubeapp.models.User;
import com.example.itubeapp.services.ServiceFactory;

public interface AuthenticateFactory extends ServiceFactory {
    void create(User user);

    void logout();

    void delete(User user);

    User login(User user);

    User register(User user);

    User update(User user);

    User getCurrentSession();
}
