package com.example.denish.interviewexperience.model;

/**
 * Created by denish on 14/8/18.
 */

public class Comment {
    String message;
    String username;
    String postid;

    public Comment() {
    }

    public Comment(String message, String username, String postid) {
        this.message = message;
        this.username = username;
        this.postid = postid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
