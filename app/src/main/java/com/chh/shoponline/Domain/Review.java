package com.chh.shoponline.Domain;

import java.io.Serializable;

public class Review implements Serializable {
    private int id;
    private String content;
    private String picUrl;
    private String time;
    private int score;
    private User user;

    public Review(){
    }

    public Review(int id, int score, String content, String picUrl, String time, User user) {
        this.id = id;
        this.content = content;
        this.picUrl = picUrl;
        this.time = time;
        this.score = score;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
