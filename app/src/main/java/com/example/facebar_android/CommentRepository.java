package com.example.facebar_android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.facebar_android.Commets.Comment;
import com.example.facebar_android.Screens.FeedActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    private CommentDao dao;
    private AppDB db;
    ArrayList<Integer> ids;
    private CommentListData commentListData;

//    private PostAPI api;

    public CommentRepository(ArrayList<Integer> ids) {
        this.ids = ids;
        db = Room.databaseBuilder(MyApplication.context, AppDB.class, "CommentsDB").fallbackToDestructiveMigration().build();
        dao = db.commentDao();
        commentListData = new CommentListData(ids);
        //api = new PostAPI(postsListDate, dao);
    }


    class CommentListData extends MutableLiveData<List<Comment>> {
        private ArrayList<Integer> ids;
        public CommentListData(ArrayList<Integer> ids) {
            super();
            this.ids = ids;

            List<Comment> comments = new ArrayList<>();
            Comment c1 = new Comment("a", "a");
            Comment c2 = new Comment("b", "b");
            Comment c3 = new Comment("c", "c");
            Comment c4 = new Comment("d", "d");
            Comment c5 = new Comment("e", "e");
            Comment c6 = new Comment("f", "f");
            Comment c7 = new Comment("g", "g");
            Comment c8 = new Comment("h", "h");
            Comment c9 = new Comment("i", "i");
            Comment c10 = new Comment("j", "j");
            comments.add(c1);
            c1.setDate((FeedActivity.getCurrentTime()));
            comments.add(c2);
            comments.add(c3);
            comments.add(c4);
            comments.add(c5);
            comments.add(c6);
            comments.add(c7);
            comments.add(c8);
            comments.add(c9);
            comments.add(c10);

//            new Thread(() -> {
              //  dao.clear();
//            }).start();
//            this.setValue(comments);
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                commentListData.postValue(dao.postComments(ids));
            }).start();
//            new Thread(() -> {
//                commentListData.postValue(dao.test());
//            }).start();

        }
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            stringBuilder.append(new String(buffer, 0, bytesRead));
        }
        return stringBuilder.toString();
    }

    public LiveData<List<Comment>> getAll() {
        return commentListData;
    }

    public void add(final Comment comment) {
        new Thread(() -> {
            long insertedId = dao.insert(comment);
            comment.setCommentId((int) insertedId); // Update the comment's ID
            ids.add(comment.getCommentId()); // Add the updated ID to the list
        }).start();
    }

    public void delete(final Comment comment) {
        new Thread(() -> {
            dao.delete(comment);
        }).start();
    }

    public void edit(final Comment comment) {
        new Thread(() -> {
            dao.update(comment);
        }).start();
    }

    public void reload() {
        new Thread(() -> {
//            commentListData.postValue(dao.test());
            commentListData.postValue(dao.postComments(ids));

        }).start();
    }
    public void removeId(Integer newId){
        this.ids.remove(newId);
    }
}
