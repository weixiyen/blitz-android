package com.blitz.app.utilities.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by mrkcsc on 9/8/14. Copyright 2014 Blitz Studios
 */
public class JsonHelper {

    // region Public Methods
    // =============================================================================================

    /**
     * Parse an array list json element.
     *
     * @param jsonElement Target json element.
     *
     * @param <T> Array list type.
     *
     * @return Array list object.
     */
    @SuppressWarnings("unused")
    public static <T> ArrayList<T> parseArrayList(JsonElement jsonElement) {

        ArrayList<T> arrayList = new ArrayList<T>();

        if (jsonElement != null && !jsonElement.isJsonNull()) {

            // Serialize appropriate array list.
            arrayList = new Gson().fromJson(jsonElement,
                    new TypeToken<ArrayList<T>>() { }.getType());
        }

        return arrayList;
    }

    /***
     * Parse a hash map json element.
     *
     * @param jsonElement Target json element.
     *
     * @param <F> First element type.
     * @param <S> Second element type.
     *
     * @return Hash map object.
     */
    @SuppressWarnings("unused")
    public static <F, S> HashMap<F, S> parseHashMap(JsonElement jsonElement) {

        HashMap<F, S> hashMap = new HashMap<F, S>();

        if (jsonElement != null && !jsonElement.isJsonNull()) {

            // Serialize appropriate hash map.
            hashMap = new Gson().fromJson(jsonElement,
                    new TypeToken<HashMap<F, S>>() { }.getType());
        }

        return hashMap;
    }

    /**
     * Parse a boolean json element.
     *
     * @param jsonElement Target json element.
     *
     * @return Bool object.
     */
    @SuppressWarnings("unused")
    public static boolean parseBool(JsonElement jsonElement) {

        return !jsonElement.isJsonNull() && jsonElement.getAsBoolean();

    }

    /**
     * Parse a int json element.
     *
     * @param jsonElement Target json element.
     *
     * @return Int object.
     */
    @SuppressWarnings("unused")
    public static int parseInt(JsonElement jsonElement) {

        if (!jsonElement.isJsonNull()) {

            return jsonElement.getAsInt();
        }

        return 0;
    }

    /**
     * Parse a date json element.
     *
     * @param jsonElement Target json element.
     *
     * @return Date object.
     */
    @SuppressWarnings("unused")
    public static Date parseDate(JsonElement jsonElement) {

        Date date = new Date();

        if (!jsonElement.isJsonNull()) {

            // Create a custom date formatter.
            Gson gsonWithDateFormat = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                    .create();

            // Parse json element into a date object.
            date = gsonWithDateFormat.fromJson(jsonElement, Date.class);
        }

        return date;
    }

    /**
     * Parse a string json element.
     *
     * @param jsonElement Target json element.
     *
     * @return String object.
     */
    @SuppressWarnings("unused")
    public static String parseString(JsonElement jsonElement) {

        String string = null;

        if (!jsonElement.isJsonNull()) {

            // Fetch the string.
            string = jsonElement.getAsString();
        }

        return string;
    }

    // endregion
}
