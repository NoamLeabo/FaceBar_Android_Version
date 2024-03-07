package com.example.facebar_android;

import androidx.lifecycle.LiveData;

import com.example.facebar_android.Commets.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentViewModel {
    private CommentRepository mRepository;

    private LiveData<List<Comment>> comments;

    public CommentViewModel(ArrayList<Integer> ids) {
        mRepository = new CommentRepository(ids);
        comments = mRepository.getAll();
    }

    public LiveData<List<Comment>> getComments() {
        return comments;
    }

    public void add(Comment comment) {
        mRepository.add(comment);
    }
    public void removeId(Integer newId){
        mRepository.removeId(newId);
    }
    public void edit(Comment comment) {
        mRepository.edit(comment);
    }
    public void delete(Comment comment) {
        mRepository.delete(comment);
    }

    public void reload() {
            mRepository.reload();
    }
}