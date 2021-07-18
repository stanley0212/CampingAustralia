package com.luvtas.campingau.Model;

public class UserModel {
    private String id, email, password, userimage, username, bio,blue_check;

    public UserModel() {
    }

    public UserModel(String id, String email, String password, String userimage, String username, String bio, String blue_check) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userimage = userimage;
        this.username = username;
        this.bio = bio;
        this.blue_check = blue_check;
    }

    public String getBlue_check() {
        return blue_check;
    }

    public void setBlue_check(String blue_check) {
        this.blue_check = blue_check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
