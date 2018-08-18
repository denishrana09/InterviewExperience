package com.example.denish.interviewexperience.model;

/**
 * Created by denish on 18/8/18.
 */

public class LikedPost {
    String userid;
    String postid;

    public LikedPost(String userid, String postid) {
        this.userid = userid;
        this.postid = postid;
    }

    public LikedPost() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
