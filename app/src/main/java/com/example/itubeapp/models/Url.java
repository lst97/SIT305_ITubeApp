package com.example.itubeapp.models;

public class Url {
    private int id;
    private final String url;
    private final int userId;

    // read from DB
    public Url(int id, String url, int userId) {
        this.id = id;
        this.url = url;
        this.userId = userId;
    }

    // create new
    public Url(String url, int userId) {
        this.url = url;
        this.userId = userId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public int getUserId() {
        return userId;
    }

}
