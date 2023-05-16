package com.example.itubeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itubeapp.R;
import com.example.itubeapp.handlers.RepositoriesHandler;
import com.example.itubeapp.models.User;
import com.example.itubeapp.persistent.UserRepository;
import com.example.itubeapp.utils.PasswordHasher;

public class RegisterActivity extends AppCompatActivity {

    EditText fullNameEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registerButton;

    UserRepository userRepository;

    private void initViews() {
        fullNameEditText = findViewById(R.id.register_full_name_input);
        usernameEditText = findViewById(R.id.register_username_input);
        passwordEditText = findViewById(R.id.register_password_input);
        confirmPasswordEditText = findViewById(R.id.register_confirm_password_input);
        registerButton = findViewById(R.id.register_create_account_btn);
    }

    private void initListeners() {
        registerButton.setOnClickListener(v -> {
            if (validateInputs()) {
                String username = usernameEditText.getText().toString();
                String passwordHash = PasswordHasher.hash(passwordEditText.getText().toString());

                userRepository.create(new User(username, passwordHash));

                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    private boolean validateInputs() {

        String fullName = fullNameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (fullName.isEmpty()) {
            fullNameEditText.setError("Full name is required");
            return false;
        }

        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Confirm password is required");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRepository = (UserRepository) RepositoriesHandler.getInstance().getRepository(UserRepository.class.getName());
        initViews();
        initListeners();

    }
}