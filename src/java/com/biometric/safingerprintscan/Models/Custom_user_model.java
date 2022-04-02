package com.biometric.safingerprintscan.Models;

public class Custom_user_model {

    String imageURL, username, timeStamp, first_name, last_name, uid, form_url;

    public Custom_user_model() {

    }

    public Custom_user_model(String imageURL, String username, String timeStamp) {
        this.imageURL = imageURL;
        this.username = username;
        this.timeStamp = timeStamp;
    }

    public Custom_user_model(String imageURL, String username, String timeStamp, String first_name, String last_name) {
        this.imageURL = imageURL;
        this.username = username;
        this.timeStamp = timeStamp;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String getForm_url() {
        return form_url;
    }

    public void setForm_url(String form_url) {
        this.form_url = form_url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
