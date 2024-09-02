package com.example.facebar_android.APP_Utilities;

import java.util.ArrayList;

/**
 * Class representing a DoubleArray which holds pairs of keys and values.
 */
public class DoubleArray {
    private ArrayList<String> keys;
    private ArrayList<String> values;

    /**
     * Default constructor initializing empty lists for keys and values.
     */
    public DoubleArray() {
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    /**
     * Constructor initializing the DoubleArray with provided keys and values.
     *
     * @param keys the list of keys
     * @param values the list of values
     */
    public DoubleArray(ArrayList<String> keys, ArrayList<String> values) {
        this.keys = keys;
        this.values = values;
    }

    /**
     * Inserts a key into the keys list.
     *
     * @param key the key to be inserted
     */
    public void insertKey(String key) {
        keys.add(key);
    }

    /**
     * Inserts a value associated with a key into the values list.
     *
     * @param name the key associated with the value
     * @param image the value to be inserted
     */
    public void insertValueToKey(String name, String image) {
        values.set(keys.indexOf(name), image);
    }

    /**
     * Inserts a pair of key and value into the keys and values lists.
     *
     * @param name the key to be inserted
     * @param image the value to be inserted
     */
    public void insertPair(String name, String image) {
        keys.add(name);
        values.add(image);
    }

    /**
     * Retrieves the value associated with a given key.
     *
     * @param key the key whose value is to be retrieved
     * @return the value associated with the key
     */
    public String getValueOfKey(String key){
        return values.get(keys.indexOf(key));
    }

    /**
     * Checks if a key exists in the keys list.
     *
     * @param key the key to be checked
     * @return true if the key exists, false otherwise
     */
    public boolean ifKeyExists(String key){
        return keys.contains(key);
    }

    /**
     * Checks if a value associated with a key exists in the values list.
     *
     * @param key the key to be checked
     * @return true if the value exists, false otherwise
     */
    public boolean ifValueOfKeyExists(String key){
        if (!ifKeyExists(key))
            return false;
        int indexOfValue = keys.indexOf(key);
        return values.size() <= indexOfValue + 1;
    }
}