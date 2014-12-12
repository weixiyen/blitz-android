package com.blitz.app.libraries.general.gson;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 12/11/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGGson {

    public enum DateFormat {
        ISO_8601
    }

    /**
     * Creates a standard gson object.
     */
    public static Gson createGson() {

        return createGson(DateFormat.ISO_8601);
    }

    /**
     * Creates a new gson object using the
     * specified date format.
     */
    public static Gson createGson(DateFormat dateFormat) {

        GsonBuilder gsonBuilder = new GsonBuilder();

        switch (dateFormat) {
            case ISO_8601:
                gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        }

        return gsonBuilder.create();
    }

    /**
     * Add multiple properties to an existing
     * json object.
     */
    @SafeVarargs
    public static JsonObject addProperties(@NonNull JsonObject jsonObject, @NonNull Pair<String, String>... properties) {

        for (Pair<String, String> property : properties) {

            jsonObject.addProperty(property.first, property.second);
        }

        return jsonObject;
    }

    /**
     * Add multiple properties to a JsonObject, creates
     * a new json object.
     */
    @SafeVarargs
    public static JsonObject addProperties(@NonNull Pair<String, String>... properties) {

        return addProperties(new JsonObject(), properties);
    }

    /**
     * Add a json object property, creating
     * a new object and returning it.
     */
    public static JsonObject addProperty(@NonNull String property,
                                         @NonNull String value) {

        return addProperty(new JsonObject(), property, value);
    }

    /**
     * Add a property to an existing json object
     * and return it for chaining purposes.S
     */
    public static JsonObject addProperty(@NonNull JsonObject jsonObject,
                                         @NonNull String property,
                                         @NonNull String value) {

        jsonObject.addProperty(property, value);

        return jsonObject;
    }
}
