package com.example.facebar_android.API;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.facebar_android.APP_Utilities.MyApplication;
import com.example.facebar_android.R;
import com.example.facebar_android.Users.ActiveUser;
import com.example.facebar_android.Users.ProfileUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersAPI {
    // retrofit variable
    Retrofit retrofit;
    // userApi variable
    UserAPI userAPI;

    /**
     * Constructor for UsersAPI.
     * Initializes the Retrofit instance and UserAPI interface.
     */
    public UsersAPI() {
        // initialize Retrofit instance
        retrofit = new Retrofit.Builder().baseUrl(MyApplication.context.getString(R.string.BaseUrl)).addConverterFactory(GsonConverterFactory.create())
                .build();
        // create UserAPI instance
        userAPI = retrofit.create(UserAPI.class);
    }

    /**
     * Fetches the token for a user.
     *
     * @param username The username of the user
     * @param password The password of the user
     * @param callback The callback to handle success or error
     */
    public void getToken(String username, String password, final AddUserCallback callback) {
        // sending the call to the server
        Call<String> call = userAPI.getToken(username, password);
        // creating the callback
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // if the response is successful
                if (response.code() == 201) {
                    // get the token from the response and update the JWT token instance
                    String token = response.body();
                    JWT jwt = JWT.getInstance();
                    jwt.upDateToken(token);
                    Log.d(TAG, "onResponse: " + jwt.getToken());
                    Log.d(TAG, "onResponse: " + token);
                    // indicate that the operation was successful
                    callback.onSuccess();
                } else {
                    String errorMessage;
                    try {
                        // get the error message from the response and pass it to the callback
                        errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }

    /**
     * Adds a new user.
     *
     * @param fName The first name of the user
     * @param lName The last name of the user
     * @param username The username of the user
     * @param password The password of the user
     * @param image The profile image of the user
     * @param callback The callback to handle success or error
     */
    public void addUser(String fName, String lName, String username, String password, String image, final AddUserCallback callback) {
        // sending the call to the server
        Call<Void> call = userAPI.newUser(fName, lName, username, password, image);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                /* if the response is successful we do nothing */

                // if the response is not successful and the user was not added
                if (response.code() == 400) {
                    // get the error message from the response and pass it to the callback
                    try {
                        String errorMessage = response.errorBody().string();
                        callback.onError(errorMessage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    // if the user was added successfully, we indicate that the operation was successful
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });

    }

    /**
     * Fetches the active user.
     *
     * @param userName The username of the user
     * @param password The password of the user
     * @param callback The callback to handle success or error
     */
    public void getUser(String userName, String password, final AddUserCallback callback) {
        // sending the call to the server
        Call<ActiveUser> call = userAPI.getUser(userName);
        // creating the callback
        call.enqueue(new Callback<ActiveUser>() {
            @Override
            public void onResponse(Call<ActiveUser> call, Response<ActiveUser> response) {
                // if the response is successful we retrieve the user information and update the ActiveUser instance
                if (response.code() == 200) {
                    ActiveUser user = response.body();

                    // initialize the user null fields
                    if (user.getLikedPosts() == null)
                        user.setLikedPosts(new ArrayList<>());
                    if (user.getFriends() == null)
                        user.setFriends(new ArrayList<>());
                    if (user.getPosts() == null)
                        user.setPosts(new ArrayList<>());
                    if (user.getPendings() == null) {
                        user.setPendings(new ArrayList<>());
                    }
                    // update the ActiveUser instance
                    ActiveUser.updateInstance(user);

                    // we make sure that the given password matches the required user password
                    if (password.equals(user.getPassword()))
                        callback.onSuccess();
                        // if the password is not correct, we pass an error message to the callback
                    else callback.onError("password not correct");
                } else {
                    // if the response is not successful, we get the error message and pass it to the callback
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
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });

    }

    /**
     * Fetches the profile user.
     *
     * @param userName The username of the user
     * @param callback The callback to handle success or error
     */
    public void getProfileUser(String userName, final AddUserCallback callback) {
        // sending the call to the server
        Call<ProfileUser> call = userAPI.getProfileUser(userName);
        // creating the callback
        call.enqueue(new Callback<ProfileUser>() {
            @Override
            public void onResponse(Call<ProfileUser> call, Response<ProfileUser> response) {
                // if the response is successful we retrieve the user information and update the ActiveUser instance
                if (response.code() == 200) {
                    ProfileUser user = response.body();

                    // initialize the user null fields
                    if (user.getLikedPosts() == null)
                        user.setLikedPosts(new ArrayList<>());
                    if (user.getFriends() == null)
                        user.setFriends(new ArrayList<>());
                    if (user.getPosts() == null)
                        user.setPosts(new ArrayList<>());
                    if (user.getReq() == null)
                        user.setReq(new ArrayList<>());

                    // update the ProfileUser instance
                    ProfileUser.updateInstance(user);
                    callback.onSuccess();
                } else {
                    // if the response is not successful, we get the error message and pass it to the callback
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
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });

    }

    /**
     * Fetches the list of friends for a user.
     *
     * @param userName The username of the user
     * @param callback The callback to handle success or error
     */
    public void getFriends(String userName, final AddUserCallback callback) {
        // construct the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<List<String>> call = userAPI.getFriends(userName, token);
        // creating the callback
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                // if the response is successful we retrieve the user information and update the ActiveUser's friends
                if (response.code() == 200) {
                    List<String> friends = response.body();
                    ArrayList<String> friendsArrayList = new ArrayList<>(friends);
                    ProfileUser.getInstance().setFriends(friendsArrayList);
                    callback.onSuccess();
                } else {
                    // if the response is not successful, we get the error message and pass it to the callback
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
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }

    /**
     * Sends a friend request.
     *
     * @param userName The username of the user sending the request
     * @param friendName The username of the user receiving the request
     * @param callback The callback to handle success or error
     */
    public void pendingFriend(String userName, String friendName, final AddUserCallback callback) {
        // construct the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<Void> call = userAPI.pendingFriend(friendName, userName, token);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // if the response is successful we indicate that the operation was successful
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    // if the response is not successful, we get the error message and pass it to the callback
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
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }

    /**
     * Accepts a friend request.
     *
     * @param userName The username of the user accepting the request
     * @param friendName The username of the user whose request is being accepted
     * @param callback The callback to handle success or error
     */
    public void acceptFriend(String userName, String friendName, final AddUserCallback callback) {
        // construct the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<Void> call = userAPI.acceptFriend(userName, friendName, token);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // if the response is successful we indicate that the operation was successful
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    // if the response is not successful, we get the error message and pass it to the callback
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
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }

    /**
     * Rejects a friend request.
     *
     * @param userName The username of the user rejecting the request
     * @param friendName The username of the user whose request is being rejected
     * @param callback The callback to handle success or error
     */
    public void rejectFriend(String userName, String friendName, final AddUserCallback callback) {
        // construct the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<Void> call = userAPI.rejectFriend(userName, friendName, token);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // if the response is successful we indicate that the operation was successful
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    // if the response is not successful, we get the error message and pass it to the callback
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
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }

    /**
     * Deletes a user.
     *
     * @param userName The username of the user to delete
     * @param callback The callback to handle success or error
     */
    public void deleteUser(String userName, final AddUserCallback callback) {
        // construct the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<Void> call = userAPI.deleteUser(userName, token);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // if the response is successful we indicate that the operation was successful
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    // if the response is not successful, we get the error message and pass it to the callback
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
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }

    /**
     * Updates a user's information.
     *
     * @param userName The username of the user to update
     * @param password The new password of the user
     * @param image The new profile image of the user
     * @param callback The callback to handle success or error
     */
    public void updateUser(String userName, String password, String image, final AddUserCallback callback) {
        // construct the token
        String token = "bearer " + JWT.getInstance().getToken();
        // sending the call to the server
        Call<Void> call = userAPI.updateUser(userName, password, image, token);
        // creating the callback
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // if the response is successful we indicate that the operation was successful
                if (response.code() == 200) {
                    callback.onSuccess();
                } else {
                    // if the response is not successful, we get the error message and pass it to the callback
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
                // if the call fails, log the error
                Log.d(TAG, "onFailure: failed");
                call.cancel();
            }
        });
    }

    /**
     * Callback interface for the user-related operations.
     */
    public interface AddUserCallback {
        void onSuccess();

        void onError(String message);
    }

}
