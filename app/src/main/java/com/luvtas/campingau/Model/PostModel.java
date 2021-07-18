package com.luvtas.campingau.Model;

public class PostModel {
    private String postid;
    private String postimage;
    private String description;
    private String publisher;
    private String sub;
    private String title;
    private Long time;
    private String username;
    private String profile_image;
    private String blue_check;
    private String type;
    private String imageType;

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBlue_check() {
        return blue_check;
    }

    public void setBlue_check(String blue_check) {
        this.blue_check = blue_check;
    }

    public PostModel() {
    }

    public PostModel(String postid, String postimage, String description, String publisher, String sub, String title, Long time, String username, String profile_image, String blue_check, String type, String imageType) {
        this.postid = postid;
        this.postimage = postimage;
        this.description = description;
        this.publisher = publisher;
        this.sub = sub;
        this.title = title;
        this.time = time;
        this.username = username;
        this.profile_image = profile_image;
        this.blue_check = blue_check;
        this.type = type;
        this.imageType = imageType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
