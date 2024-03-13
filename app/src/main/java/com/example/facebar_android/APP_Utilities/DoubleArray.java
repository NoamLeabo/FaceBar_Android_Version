package com.example.facebar_android.APP_Utilities;

import java.util.ArrayList;

public class DoubleArray {
    private ArrayList<String> keys;
    private ArrayList<String> values;

    public DoubleArray() {
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
    }

    public DoubleArray(ArrayList<String> keys, ArrayList<String> values) {
        this.keys = keys;
        this.values = values;
    }

    public void insertKey(String key) {
        keys.add(key);
    }

    public void insertValueToKey(String name, String image) {
        values.set(keys.indexOf(name), image);
    }

    public void insertPair(String name, String image) {
        keys.add(name);
        values.add(image);
    }
    public String getValueOfKey(String key){
        return values.get(keys.indexOf(key));
    }
    public boolean ifKeyExists(String key){
        if (!keys.contains(key))
            return false;
        else return true;
    }
    public boolean ifValueOfKeyExists(String key){
        if (!ifKeyExists(key))
            return false;
        int indexOfValue = keys.indexOf(key);
        if (values.size() > indexOfValue + 1)
            return false;
        else return true;
    }
}
