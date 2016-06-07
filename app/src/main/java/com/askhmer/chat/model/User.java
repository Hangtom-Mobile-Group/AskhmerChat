package com.askhmer.chat.model;

/**
 * Created by soklundy on 5/28/2016.
 */
public class User {
    private String userId;
    private String userName;
    private String gender;
    private String userNo;
    private String userPhoto;
    private String userEmail;
    private String userPassword;
    private String userHometown;
    private String userCurrentCity;
    private String userPhoneNum;
    private String facebookId;
    private String userAccessToken;
    private String userRigisDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserHometown() {
        return userHometown;
    }

    public void setUserHometown(String userHometown) {
        this.userHometown = userHometown;
    }

    public String getUserCurrentCity() {
        return userCurrentCity;
    }

    public void setUserCurrentCity(String userCurrentCity) {
        this.userCurrentCity = userCurrentCity;
    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getUserAccessToken() {
        return userAccessToken;
    }

    public void setUserAccessToken(String userAccessToken) {
        this.userAccessToken = userAccessToken;
    }

    public String getUserRigisDate() {
        return userRigisDate;
    }

    public void setUserRigisDate(String userRigisDate) {
        this.userRigisDate = userRigisDate;
    }

    public User(String userId, String userName, String gender, String userNo, String userPhoto, String userEmail, String userPassword, String userHometown, String userCurrentCity, String userPhoneNum, String facebookId, String userAccessToken, String userRigisDate) {
        this.userId = userId;
        this.userName = userName;
        this.gender = gender;
        this.userNo = userNo;
        this.userPhoto = userPhoto;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userHometown = userHometown;
        this.userCurrentCity = userCurrentCity;
        this.userPhoneNum = userPhoneNum;
        this.facebookId = facebookId;
        this.userAccessToken = userAccessToken;
        this.userRigisDate = userRigisDate;
    }

    public User() {

    }
}
