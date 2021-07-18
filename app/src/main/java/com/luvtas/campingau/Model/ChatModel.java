package com.luvtas.campingau.Model;

public class ChatModel {
    private String sender;
    private String receiver;
    private String message;
    private String userphoto;
    private boolean isseen;
    private long lastUpdate;

    public ChatModel(String sender, String receiver, String message, String userphoto, boolean isseen, long lastUpdate) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.userphoto = userphoto;
        this.isseen = isseen;
        this.lastUpdate = lastUpdate;
    }

    public ChatModel() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
