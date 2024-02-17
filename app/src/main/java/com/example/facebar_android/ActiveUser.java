package com.example.facebar_android;

public class ActiveUser {
    private String fName;
    private String lName;
    private String username;
    private String password;
    private int profileImage = R.drawable.pic3;

    public ActiveUser(String fName, String lName, String username, String password, int profileImage) {
        this.fName = fName;
        this.lName = lName;
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
    }
    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
}
