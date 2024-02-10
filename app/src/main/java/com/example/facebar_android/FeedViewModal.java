package com.example.facebar_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class FeedViewModal extends ViewModel {
    private PostRepository mRepository;

    private LiveData<List<Post>> posts;

    public FeedViewModal () {
        mRepository = new PostRepository();
        posts = mRepository.getAll();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }
    public void add(Post post) {
        mRepository.add(post);
    }

    public void dele te(Post post) {
        mRepository.delete(post);
    }

    public void reload(Post post) {
        mRepository.reload();
    }
}
