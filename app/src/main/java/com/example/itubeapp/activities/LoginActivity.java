package com.example.itubeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itubeapp.R;
import com.example.itubeapp.handlers.RepositoriesHandler;
import com.example.itubeapp.handlers.ServicesHandler;
import com.example.itubeapp.helpers.RepositoryHelper;
import com.example.itubeapp.helpers.ServiceHelper;
import com.example.itubeapp.models.User;
import com.example.itubeapp.persistent.UrlRepository;
import com.example.itubeapp.persistent.UserRepository;
import com.example.itubeapp.services.authenticate.AuthenticateService;
import com.example.itubeapp.services.authenticate.session.SessionService;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registerButton;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private AuthenticateService authenticateService;

    private boolean validateInput() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }

        return true;
    }

    private void initViews() {
        loginButton = findViewById(R.id.login_login_btn);
        registerButton = findViewById(R.id.login_register_btn);
        usernameEditText = findViewById(R.id.login_username_input);
        passwordEditText = findViewById(R.id.login_password_input);
    }

    private void initListeners() {
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

            if (validateInput()) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // BCrypt library is designed to compare a plain password with a previously hashed password
                // So plain text password is provided into the User Model
                if (authenticateService.login(new User(username, password)) == null) {
                    usernameEditText.setError("Username or password is incorrect");
                    return;
                }
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void initServices() {
        ServicesHandler servicesHandler = ServicesHandler.getInstance(new ServiceHelper(this, ServicesHandler.class.getName()));

        // For authenticateService to store session using shared preferences
        SessionService sessionService = new SessionService(new ServiceHelper(this, SessionService.class.getName()));
        servicesHandler.addService(sessionService);

        authenticateService = new AuthenticateService(new ServiceHelper(this, AuthenticateService.class.getName()));
        servicesHandler.addService(authenticateService);
    }

    private void initRepositories() {
        String DATABASE_NAME = "8_1C";

        RepositoriesHandler repositoriesHandler = RepositoriesHandler.getInstance(new RepositoryHelper(RepositoriesHandler.class.getName()));

        // it will create the table if it doesn't exist
        UserRepository userRepository = new UserRepository(openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null), UserRepository.class.getName());
        repositoriesHandler.addRepository(userRepository);

        UrlRepository urlRepository = new UrlRepository(openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null), UrlRepository.class.getName());
        repositoriesHandler.addRepository(urlRepository);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initListeners();
        initRepositories();
        initServices();
    }
}