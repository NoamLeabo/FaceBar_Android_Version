package com.example.facebar_android.APP_Utilities;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Class containing type converters for Room database.
 * These converters are used to convert complex data types to and from JSON strings.
 */
public class Converters {

    /**
     * Converts a JSON string to a List of Integers.
     *
     * @param value the JSON string representing a list of integers
     * @return the List of Integers
     */
    @TypeConverter
    public static List<Integer> integerListFromString(String value) {
        Type listType = new TypeToken<List<Integer>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    /**
     * Converts a List of Integers to a JSON string.
     *
     * @param list the List of Integers
     * @return the JSON string representing the list of integers
     */
    @TypeConverter
    public static String integerListToString(List<Integer> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    /**
     * Converts a JSON string to an ArrayList of Strings.
     *
     * @param value the JSON string representing an ArrayList of strings
     * @return the ArrayList of Strings
     */
    @TypeConverter
    public static ArrayList<String> stringArrayListFromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    /**
     * Converts an ArrayList of Strings to a JSON string.
     *
     * @param list the ArrayList of Strings
     * @return the JSON string representing the ArrayList of strings
     */
    @TypeConverter
    public static String stringArrayListToString(ArrayList<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}