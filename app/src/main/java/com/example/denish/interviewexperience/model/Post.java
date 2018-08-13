package com.example.denish.interviewexperience.model;

/**
 * Created by denish on 13/8/18.
 */

public class Post {
    String title;
    String company;
    String position;
    String description;

    public Post() {
    }

    public Post(String title, String company, String position, String description) {
        this.title = title;
        this.company = company;
        this.position = position;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
