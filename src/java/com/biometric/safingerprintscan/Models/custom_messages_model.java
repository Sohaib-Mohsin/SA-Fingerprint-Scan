package com.biometric.safingerprintscan.Models;

public class custom_messages_model {

    String imageURL, Username, Last_msg, time, status, uid;

    public custom_messages_model(String imageURL, String username, String last_msg, String time, String status, String uid) {
        this.imageURL = imageURL;
        Username = username;
        Last_msg = last_msg;
        this.time = time;
        this.status = status;
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public custom_messages_model() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getLast_msg() {
        return Last_msg;
    }

    public void setLast_msg(String last_msg) {
        Last_msg = last_msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
