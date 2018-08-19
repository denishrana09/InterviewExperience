package com.example.denish.interviewexperience.model;

/**
 * Created by denish on 13/8/18.
 */

public class Post {
    String company;
    String position;
    String description;
    String userid;
    String date;
    int views;
    int comments;

    public Post() {
    }

    public Post(String company, String position, String description, String userid, String date, int views, int comments) {
        this.company = company;
        this.position = position;
        this.description = description;
        this.userid = userid;
        this.date = date;
        this.views = views;
        this.comments = comments;
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
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

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "company='" + company + '\'' +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", userid='" + userid + '\'' +
                ", date='" + date + '\'' +
                ", views=" + views +
                ", comments=" + comments +
                '}';
    }
}
