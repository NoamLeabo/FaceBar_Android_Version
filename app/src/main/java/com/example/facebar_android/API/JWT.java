package com.example.facebar_android.API;

/**
 * Singleton class for managing JWT (JSON Web Token).
 */
public class JWT {
    // Static instance of the JWT class
    private static JWT instance = null;
    // Token string
    private String token;

    /**
     * Private constructor to prevent instantiation.
     * Initializes the token with an empty string.
     */
    private JWT() {
        this.token = " ";
    }

    /**
     * Returns the singleton instance of the JWT class.
     * If the instance is null, it creates a new one.
     *
     * @return the singleton instance of JWT
     */
    public static JWT getInstance() {
        if (instance == null) {
            instance = new JWT();
        }
        return instance;
    }

    /**
     * Gets the current token.
     *
     * @return the current token
     */
    public String getToken() {
        return token;
    }

    /**
     * Updates the token with a new value.
     *
     * @param token the new token value
     */
    public void upDateToken(String token) {
        this.token = token;
    }
}