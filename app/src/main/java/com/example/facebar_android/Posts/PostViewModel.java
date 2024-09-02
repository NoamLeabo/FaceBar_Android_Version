package com.example.facebar_android.Posts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * ViewModel class for managing Post data.
 * Provides methods for accessing and manipulating Post data through the repository.
 */
public class PostViewModel extends ViewModel {
    private PostRepository mRepository;
    private LiveData<List<Post>> posts;
    private String username;

    /**
     * Constructor for initializing the PostViewModel.
     *
     * @param username the username of the current user
     */
    public PostViewModel(String username) {
        mRepository = new PostRepository(username);
        posts = mRepository.getAll();
        this.username = username;
    }

    /**
     * Retrieves the list of posts as LiveData.
     *
     * @return LiveData containing the list of posts
     */
    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    /**
     * Reloads the posts for the current user.
     */
    public void reloadUserPost() {
        mRepository.reloadUserPost();
    }

    /**
     * Adds a new post.
     *
     * @param post the post to add
     */
    public void add(Post post) {
        mRepository.add(post);
    }

    /**
     * Edits an existing post.
     *
     * @param post the post to edit
     */
    public void edit(Post post) {
        mRepository.edit(post);
    }

    /**
     * Deletes a post.
     *
     * @param post the post to delete
     */
    public void delete(Post post) {
        mRepository.delete(post);
    }

    /**
     * Reloads all posts.
     */
    public void reload() {
        mRepository.reload();
    }

    /**
     * Likes a post.
     *
     * @param post the post to like
     */
    public void likePost(Post post) {
        mRepository.likePost(post, username);
    }
}