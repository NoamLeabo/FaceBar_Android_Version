package com.example.facebar_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.facebar_android.Posts.Post;

import java.util.List;

public class PostViewModel extends ViewModel {
    private PostRepository mRepository;

    private LiveData<List<Post>> posts;

    public PostViewModel(String username) {
            mRepository = new PostRepository(username);
            posts = mRepository.getAll();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }
    public LiveData<List<Post>> getUserPosts(String username) {
        mRepository.getUserPost(username);
        return posts;
    }
    public void reloadUserPost() {
        mRepository.reloadUserPost();
    }


    public void add(Post post) {
            mRepository.add(post);
    }
    public void edit(Post post) {
        mRepository.edit(post);
    }
    public void delete(Post post) {
            mRepository.delete(post);
    }

    public void reload() {
            mRepository.reload();
    }
}