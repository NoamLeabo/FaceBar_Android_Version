package com.example.facebar_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PostViewModal extends ViewModel {
    // private PostRepository mRepository;

    private LiveData<List<Post>> posts;

    public PostViewModal () {
        //    mRepository = new PostRepository();
        //    posts = mRepository.getAll();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }
    public void add(Post post) {
        //    mRepository.add(post);
    }

    public void delete(Post post) {
        //    mRepository.delete(post);
    }

    public void reload(Post post) {
        //    mRepository.reload();
    }
}
