package com.example.facebar_android;

import java.util.ArrayList;
import java.util.List;

public class PostsList {
    private List<Post> postList = new ArrayList<>();

    public PostsList(List<Post> postList) {}

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }
    public void addPost(Post post) {
        this.postList.add(post);
    }
    public void deletePost(Post post) {
        int indexToDelete = postList.indexOf(post);
        this.postList.remove(indexToDelete);
    }
}
