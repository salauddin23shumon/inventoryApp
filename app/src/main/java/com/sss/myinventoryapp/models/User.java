package com.sss.myinventoryapp.models;

import java.util.Map;

public class User {

    private String uid;
    private String userName;
    private String email;
    private String contact;
    private String userImageUrl;



    public User() {
    }

    public User(String uid, String userName, String email) {
        this.uid = uid;
        this.userName = userName;
        this.email = email;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
