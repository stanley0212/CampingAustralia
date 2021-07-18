package com.luvtas.campingau.Model;

public class NotificationModel {
    private String userid;
    private String comment;
    private String postid;
    private Long time;
    private boolean ispost;

    public NotificationModel(String userid, String comment, String postid, boolean ispost, Long time) {
        this.userid = userid;
        this.comment = comment;
        this.postid = postid;
        this.ispost = ispost;
        this.time = time;
    }

    public NotificationModel() {
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}
