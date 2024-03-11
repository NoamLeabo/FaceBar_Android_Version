package com.example.facebar_android;

public class JWT {
    private static JWT instance = null;
    private String token;

    private JWT() {
        this.token = " ";
    }

    public static JWT getInstance() {
        if (instance == null) {
            instance = new JWT();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }
    public void upDateToken(String token) {
        this.token = token;
    }
}
