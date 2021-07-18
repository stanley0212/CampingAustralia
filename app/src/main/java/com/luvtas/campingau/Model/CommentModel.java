package com.luvtas.campingau.Model;

public class CommentModel {
    private String comment;
    private String publisher;
    private Long time;
    private String commentid;

    public CommentModel() {
    }

    public CommentModel(String comment, String publisher, Long time, String commentid) {
        this.comment = comment;
        this.publisher = publisher;
        this.time = time;
        this.commentid = commentid;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
