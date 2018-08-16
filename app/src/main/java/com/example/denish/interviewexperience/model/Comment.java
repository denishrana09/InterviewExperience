package com.example.denish.interviewexperience.model;

/**
 * Created by denish on 14/8/18.
 */

public class Comment {
    String message;
    String userid;
    String postid;

    public Comment() {
    }

    public Comment(String message, String userid, String postid) {
        this.message = message;
        this.userid = userid;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
