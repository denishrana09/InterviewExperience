package com.example.denish.interviewexperience.model;

/**
 * Created by denish on 13/8/18.
 */

public class Post {
    String title;
    String company;
    String position;
    String description;
    String userid;
    String date;
    int likes;

    public Post() {
    }

    public Post(String title, String company, String position, String description, String userid, String date, int likes) {
        this.title = title;
        this.company = company;
        this.position = position;
        this.description = description;
        this.userid = userid;
        this.date = date;
        this.likes = likes;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
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
