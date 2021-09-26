package com.example.newprojectmobileapp.model;

public class Comment {
    String userUid;
    String userName;
    String movieTitle;
    String commentContent;
    String commentTime;
    int countLike;

    public Comment() {
    }

    public Comment(String userUid, String userName, String movieTitle, String commentContent, String commentTime, int countLike) {
        this.userUid = userUid;
        this.userName = userName;
        this.movieTitle = movieTitle;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
        this.countLike = countLike;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }
}
