package com.application.medicare.medicare;

/**
 * Created by user1 on 2017-10-31.
 */
public class Data {
    private String data1;
    private String userId;
    private String userPassword;
    private String token ;

    public String getToken() { return token; }

    public void setToken(String token) { this.token =token;}

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}