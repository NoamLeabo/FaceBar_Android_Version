package com.example.facebar_android.Users;

import java.util.ArrayList;

public class ProfileUser {
    private String fName;
    private String lName;
    private String username;
    private String password;
    private String profileImg =" R.drawable.pic3";
    private ArrayList<Integer> posts = new ArrayList<>();
    private ArrayList<Integer> likedPosts = new ArrayList<>();
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> pending = new ArrayList<>();

    private ArrayList<String> req = new ArrayList<>();
    private static ProfileUser instance;
    public static void updateInstance(ProfileUser user) {
        instance = user;
    }
    public static ProfileUser getInstance() {
        if (instance == null) {
            // You may throw an IllegalStateException here or return null depending on your requirement
            throw new IllegalStateException("ProfileUser instance has not been initialized.");
        }
        return instance;
    }
    public ProfileUser(String username, String password, String profileImage) {
        this.username = username;
        this.password = password;
        this.profileImg = profileImage;
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

    public String getProfileImage() {
        return profileImg;
    }

    public void setProfileImage(String profileImage) {
        this.profileImg = profileImage;
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

    public ArrayList<String> getPending() {
        return pending;
    }

    public void setPending(ArrayList<String> pending) {
        this.pending = pending;
    }
}