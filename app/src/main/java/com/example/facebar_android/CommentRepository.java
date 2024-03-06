//package com.example.facebar_android;
//
//import android.content.Context;
//import android.content.res.AssetManager;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.room.Room;
//
//import com.example.facebar_android.Commets.Comment;
//import com.example.facebar_android.Posts.Post;
//import com.example.facebar_android.Screens.FeedActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CommentRepository {
//    private CommentDao dao;
//    private AppDB db;
//    private CommentRepository.CommentListData commentListData;
//
////    private PostAPI api;
//
//    public CommentRepository(ArrayList<Integer> ids) {
//        db = Room.databaseBuilder(MyApplication.context, AppDB.class, "CommentsDB").build();
//        dao = db.commentDao();
//        commentListData = new CommentListData(ids);
//        //api = new PostAPI(postsListDate, dao);
//    }
//
//
//    class CommentListData extends MutableLiveData<List<Comment>> {
//        private ArrayList<Integer> ids;
//        public CommentListData(ArrayList<Integer> ids) {
//            super();
//            this.ids = ids;
//
////            List<Comment> comments = new ArrayList<>();
////            this.setValue(comments);
//        }
//
//        @Override
//        protected void onActive() {
//            super.onActive();
//
//            new Thread(() -> {
//                commentListData.postValue(dao.get(ids));
//            }).start();
//        }
//    }
//
//    private String inputStreamToString(InputStream inputStream) throws IOException {
//        StringBuilder stringBuilder = new StringBuilder();
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            stringBuilder.append(new String(buffer, 0, bytesRead));
//        }
//        return stringBuilder.toString();
//    }
//
//    public LiveData<List<Comment>> getAll() {
//        return commentListData;
//    }
//
//    public void add(final Comment comment) {
//        new Thread(() -> {
//            dao.insert(comment);
//        }).start();
//    }
//
//    public void delete(final Comment comment) {
//        new Thread(() -> {
//            dao.delete(comment);
//        }).start();
//    }
//
//    public void edit(final Comment comment) {
//        new Thread(() -> {
//            dao.update(comment);
//        }).start();
//    }
//
////    public void reload() {
////        api.get();
////    }
//}
