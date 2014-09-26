package com.blitz.app.utilities.json;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Date;
/**
 * Created by mrkcsc on 9/8/14. Copyright 2014 Blitz Studios
 */
public class JsonHelper {

    // region Public Methods
    // =============================================================================================

    /**
     * Create a standard builder that
     * can parse our dates and complex objects.
     *
     * @return Gson builder.
     */
    @SuppressWarnings("unused")
    public static Gson builder() {

        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
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

        return jsonElement != null &&
              !jsonElement.isJsonNull() && jsonElement.getAsBoolean();
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

        return jsonElement != null &&
              !jsonElement.isJsonNull() ? jsonElement.getAsInt() : 0;
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

        if (jsonElement != null &&
           !jsonElement.isJsonNull()) {

            // Parse json element into a date object.
            return builder().fromJson(jsonElement, Date.class);
        }

        return null;
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

        if (jsonElement != null &&
           !jsonElement.isJsonNull()) {

            // Fetch the string.
            return jsonElement.getAsString();
        }

        return null;
    }

    // endregion
}