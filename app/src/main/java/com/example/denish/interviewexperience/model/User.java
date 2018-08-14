package com.example.denish.interviewexperience.model;

/**
 * Created by denish on 14/8/18.
 */

public class User {
    String username;
    String photopath;
    String description;
    String email;

    public User() {
    }

    public User(String username, String photopath, String description, String email) {
        this.username = username;
        this.photopath = photopath;
        this.description = description;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
