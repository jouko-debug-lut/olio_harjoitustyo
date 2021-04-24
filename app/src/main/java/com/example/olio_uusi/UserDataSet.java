package com.example.olio_uusi;

public class UserDataSet {

    //private String userId;
    private String userName;
    private String userHeight;
    private String userWeight;
    private String userAge;
    private String userGender;

    public UserDataSet() {
    }

    public UserDataSet(String userName, String userHeight, String userWeight, String userAge, String userGender) {
        //this.userId = userId;
        this.userName = userName;
        this.userHeight = userHeight;
        this.userWeight = userWeight;
        this.userAge = userAge;
        this.userGender = userGender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }

    public String getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}
