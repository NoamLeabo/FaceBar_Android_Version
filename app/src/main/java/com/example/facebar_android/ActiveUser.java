package com.example.facebar_android;

import java.util.ArrayList;

public class ActiveUser {
    private String fName;
    private String lName;
    private String username;
    private String password;
    private int profileImage = R.drawable.pic3;
    private ArrayList<Integer> posts = new ArrayList<>();
    private ArrayList<Integer> likedPosts = new ArrayList<>();
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> req = new ArrayList<>();

    public ActiveUser(String username, String password, int profileImage) {
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

    public ArrayList<Integer> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Integer> posts) {
        this.posts = posts;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getReq() {
        return req;
    }

    public void setReq(ArrayList<String> req) {
        this.req = req;
    }

    public ArrayList<Integer> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(ArrayList<Integer> likedPosts) {
        this.likedPosts = likedPosts;
    }
}
