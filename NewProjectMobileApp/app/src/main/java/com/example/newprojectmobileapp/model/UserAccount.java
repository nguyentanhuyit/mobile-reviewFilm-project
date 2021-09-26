package com.example.newprojectmobileapp.model;

public class UserAccount {
    String userUid;
    String userEmail;
    String userPassword;
    String userName;
    String userAvatar;
    int userAge;
    boolean userGender; // male: true
    boolean userVerified;
    String accountType;
    int accountLevel;
    int accountStatus; // 1 : active

    public UserAccount() {
    }

    public UserAccount(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public UserAccount(String userUid, String userEmail, String userPassword, String userName,
                       String userAvatar, int userAge, boolean userGender, boolean userVerified,
                       String accountType, int accountLevel, int accountStatus) {
        this.userUid = userUid;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.userAge = userAge;
        this.userGender = userGender;
        this.userVerified = userVerified;
        this.accountType = accountType;
        this.accountLevel = accountLevel;
        this.accountStatus = accountStatus;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public boolean isUserGender() {
        return userGender;
    }

    public void setUserGender(boolean userGender) {
        this.userGender = userGender;
    }

    public boolean isUserVerified() {
        return userVerified;
    }

    public void setUserVerified(boolean userVerified) {
        this.userVerified = userVerified;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getAccountLevel() {
        return accountLevel;
    }

    public void setAccountLevel(int accountLevel) {
        this.accountLevel = accountLevel;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }
}
