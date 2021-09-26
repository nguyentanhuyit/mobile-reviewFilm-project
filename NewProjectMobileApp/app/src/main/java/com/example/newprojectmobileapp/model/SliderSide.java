package com.example.newprojectmobileapp.model;

public class SliderSide {
    String videoId;
    String videoName;
    String videoUrl;
    String videoCategory;
    String videoType;
    String videoThumbnail;
    boolean videoSlide;
    String videoTitle;
    String videoDirector;
    String videoMainActor;
    String videoNation;
    String videoYear;
    String videoLength;
    String videoDescription;
    int videoView;

    public SliderSide() {
    }

    public SliderSide(String videoId, String videoName, String videoUrl, String videoCategory,
                      String videoType, String videoThumbnail, boolean videoSlide, String videoTitle,
                      String videoDirector, String videoMainActor, String videoNation, String videoYear,
                      String videoLength, String videoDescription, int videoView) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoUrl = videoUrl;
        this.videoCategory = videoCategory;
        this.videoType = videoType;
        this.videoThumbnail = videoThumbnail;
        this.videoSlide = videoSlide;
        this.videoTitle = videoTitle;
        this.videoDirector = videoDirector;
        this.videoMainActor = videoMainActor;
        this.videoNation = videoNation;
        this.videoYear = videoYear;
        this.videoLength = videoLength;
        this.videoDescription = videoDescription;
        this.videoView = videoView;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoCategory() {
        return videoCategory;
    }

    public void setVideoCategory(String videoCategory) {
        this.videoCategory = videoCategory;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public boolean isVideoSlide() {
        return videoSlide;
    }

    public void setVideoSlide(boolean videoSlide) {
        this.videoSlide = videoSlide;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDirector() {
        return videoDirector;
    }

    public void setVideoDirector(String videoDirector) {
        this.videoDirector = videoDirector;
    }

    public String getVideoMainActor() {
        return videoMainActor;
    }

    public void setVideoMainActor(String videoMainActor) {
        this.videoMainActor = videoMainActor;
    }

    public String getVideoNation() {
        return videoNation;
    }

    public void setVideoNation(String videoNation) {
        this.videoNation = videoNation;
    }

    public String getVideoYear() {
        return videoYear;
    }

    public void setVideoYear(String videoYear) {
        this.videoYear = videoYear;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public int getVideoView() {
        return videoView;
    }

    public void setVideoView(int videoView) {
        this.videoView = videoView;
    }
}
