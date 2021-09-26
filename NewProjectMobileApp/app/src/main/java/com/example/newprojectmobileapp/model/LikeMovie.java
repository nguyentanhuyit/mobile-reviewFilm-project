package com.example.newprojectmobileapp.model;

import java.io.Serializable;

public class LikeMovie implements Serializable {
    String userEmail;
    boolean like;

    public LikeMovie() {
    }

    public LikeMovie(String userEmail, boolean like) {
        this.userEmail = userEmail;
        this.like = like;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
