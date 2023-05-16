package com.example.itubeapp.models;

import java.io.Serializable;

public class User implements Serializable {
    private final String password;
    private int id;
    private String name;

    // For database mapping
    public User(int id, String name, String hashedPassword) {
        this.id = id;
        this.name = name;
        this.password = hashedPassword;
    }

    // For create new user
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // password maybe hashed or plain text
    public String getPassword() {
        return password;
    }
}