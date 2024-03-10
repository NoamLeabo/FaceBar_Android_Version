package com.example.facebar_android;

import static android.content.ContentValues.TAG;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class usersAPI {
    Retrofit retrofit;
    UserAPI userAPI;
    public usersAPI() {
        retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:12345/").addConverterFactory(GsonConverterFactory.create())
                .build();
        userAPI = retrofit.create(UserAPI.class);
    }
    public void getToken(String username,String password, final AddUserCallback callback) {
        Call<String> call = userAPI.getToken(username, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 201) {
                    String token = response.body();
                    JWT jwt = JWT.getInstance();
                    jwt.upDateToken(token);
                    Log.d(TAG, "onResponse: "+jwt.getToken());
                    Log.d(TAG, "onResponse: "+token);
                    callback.onSuccess();
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }
    public void addUser(String fName, String lName, String username, String password, String image, final AddUserCallback callback) {
        Call<Void> call = userAPI.newUser(fName, lName, username, password, image);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 400) {
                    try {
                        String errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    callback.onSuccess();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });

    }

    public void getUser(String userName, String password, final AddUserCallback callback) {
        Call<ActiveUser> call = userAPI.getUser(userName);
        call.enqueue(new Callback<ActiveUser>() {
            @Override
            public void onResponse(Call<ActiveUser> call, Response<ActiveUser> response) {
                if (response.code() == 200) {
                    ActiveUser user = response.body();
                    if (user.getLikedPosts() == null)
                        user.setLikedPosts(new ArrayList<>());
                    if (user.getFriends() == null)
                        user.setFriends(new ArrayList<>());
                    if (user.getPosts() == null)
                        user.setPosts(new ArrayList<>());
                    if (user.getPendings() == null) {
                        ArrayList<String> p = new ArrayList<>();
                        p.add("GIGI");
                        p.add("DIDI");
                        user.setPendings(p);
                    }
                    ActiveUser.updateInstance(user);
                    if (password.equals(user.getPassword()))
                        callback.onSuccess();
                    else callback.onError("password not correct");
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ActiveUser> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });

    }

    public void getProfileUser(String userName, final AddUserCallback callback) {
        Call<ProfileUser> call = userAPI.getProfileUser(userName);
        call.enqueue(new Callback<ProfileUser>() {
            @Override
            public void onResponse(Call<ProfileUser> call, Response<ProfileUser> response) {
                if (response.code() == 200) {
                    ProfileUser user = response.body();
                    if (user.getLikedPosts() == null)
                        user.setLikedPosts(new ArrayList<>());
                    if (user.getFriends() == null)
                        user.setFriends(new ArrayList<>());
                    if (user.getPosts() == null)
                        user.setPosts(new ArrayList<>());
                    if (user.getReq() == null)
                        user.setReq(new ArrayList<>());
                    ProfileUser.updateInstance(user);
                    callback.onSuccess();
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<ProfileUser> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });

    }

    public void getFriends(String userName, final AddUserCallback callback) {
        Call<List<String>> call = userAPI.getFriends(userName);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.code() == 200) {
                    List<String> friends =response.body();
                    ArrayList<String> friendsArrayList = new ArrayList<>(friends);
                    ActiveUser.getInstance().setFriends(friendsArrayList);
                    callback.onSuccess();
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }

    public void pendingFriend(String userName, String friendName, final AddUserCallback callback) {
        Call<Void> call = userAPI.pendingFriend(userName, friendName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }
    public void acceptFriend(String userName, String friendName, final AddUserCallback callback) {
        Call<Void> call = userAPI.acceptFriend(userName, friendName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }
    public void rejectFriend(String userName, String friendName, final AddUserCallback callback) {
        Call<Void> call = userAPI.rejectFriend(userName, friendName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }
    public void deleteUser(String userName, final AddUserCallback callback) {
        Call<Void> call = userAPI.deleteUser(userName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }
    public void updateUser(String userName, String password,String image, final AddUserCallback callback) {
        Call<Void> call = userAPI.updateUser(userName,password, image);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }

    public interface AddUserCallback {
        void onSuccess();
        void onError(String message);
    }

}
