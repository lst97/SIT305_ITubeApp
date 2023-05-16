package com.example.itubeapp.services.authenticate;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.itubeapp.handlers.RepositoriesHandler;
import com.example.itubeapp.handlers.ServicesHandler;
import com.example.itubeapp.helpers.PreferenceType;
import com.example.itubeapp.helpers.ServiceHelper;
import com.example.itubeapp.models.User;
import com.example.itubeapp.persistent.UserRepository;
import com.example.itubeapp.services.authenticate.session.SessionService;
import com.example.itubeapp.services.log.LogService;
import com.example.itubeapp.services.log.LogTypes;
import com.example.itubeapp.utils.PasswordHasher;

public class AuthenticateService implements AuthenticateFactory {
    private final String serviceName;
    private final LogService logService;
    private final SharedPreferences sharedPreferences;
    private final UserRepository userRepository;
    private boolean rememberMe = false;

    public AuthenticateService(ServiceHelper helper) {
        this.serviceName = helper.getServiceName();
        this.userRepository = (UserRepository) RepositoriesHandler.getInstance().getRepository(UserRepository.class.getName());

        if (helper.isLogEnabled()) {
            this.logService = (LogService) ServicesHandler.getInstance().getService(LogService.class.getName());
        } else {
            this.logService = null;
        }

        this.sharedPreferences = helper.getContext().getSharedPreferences(PreferenceType.REMEMBER_ME, Context.MODE_PRIVATE);

    }

    private void setRememberMe(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PreferenceType.REMEMBER_ME, value);
        editor.apply();
    }

    public void enableRememberMe() {
        rememberMe = true;
        setRememberMe(true);
    }

    public void disableRememberMe() {
        rememberMe = false;
        setRememberMe(false);
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public void onCreate() {
        if (logService != null) {
            logService.log(serviceName + " Created", LogTypes.INFO);
        }
    }

    @Override
    public void create(User user) {
        // to make sure every hash is unique in the database
        // should use serialize the password with the username and then hash it
        // for example: test1:password -> PasswordHasher.hash("test1:password")
        userRepository.create(user);
    }

    @Override
    public void logout() {
        // not required for the task
        rememberMe = false;
        setRememberMe(false);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    /**
     * Shared Preferences are used to store the user's login information
     *
     * @param user User object containing the user's login information
     * @return User object containing the user's login information
     */
    @Override
    public User login(User user) {
        User userFromDB = userRepository.read(user.getName());
        if (userFromDB != null && PasswordHasher.verify(user.getPassword(), userFromDB.getPassword())) {
            if (rememberMe) {
                enableRememberMe();
            } else {
                disableRememberMe();
            }
            setCurrentSession(userFromDB);
            return userFromDB;
        }
        return null;
    }

    @Override
    public User register(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User getCurrentSession() {
        SessionService sessionService = (SessionService) ServicesHandler.getInstance().getService(SessionService.class.getName());
        return sessionService.getSession();
    }

    private void setCurrentSession(User user) {
        SessionService sessionService = (SessionService) ServicesHandler.getInstance().getService(SessionService.class.getName());
        sessionService.setSession(user);
    }

    public boolean isRememberMeEnabled() {
        return rememberMe;
    }
}
