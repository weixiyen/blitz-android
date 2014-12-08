package com.blitz.app.utilities.json;

import android.support.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by mrkcsc on 12/7/14. Copyright 2014 Blitz Studios
 */
public class JsonHelperObject {

    public static JsonObject addProperties(@NonNull List<String> properties,
                                           @NonNull List<String> values) {

        JsonObject jsonObject = new JsonObject();

        if (properties.size() == values.size()) {

            for (int i = 0; i < properties.size(); i++) {

                jsonObject.addProperty(properties.get(i), values.get(i));
            }
        }

        return jsonObject;
    }

    public static JsonObject addProperty(@NonNull String property,
                                         @NonNull String value) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(property, value);

        return jsonObject;
    }

    public static JsonObject add(@NonNull String property,
                                 @NonNull JsonElement jsonElement) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.add(property, jsonElement);

        return jsonObject;
    }
}