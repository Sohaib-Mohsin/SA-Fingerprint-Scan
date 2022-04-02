package com.biometric.safingerprintscan.Models;

public class Chat_Model {

    String message, time, sent, imageURL ,uid;

    public Chat_Model() {
    }

    public Chat_Model(String message, String time, String sent, String imageURL, String uid) {
        this.message = message;
        this.time = time;
        this.sent = sent;
        this.imageURL = imageURL;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
