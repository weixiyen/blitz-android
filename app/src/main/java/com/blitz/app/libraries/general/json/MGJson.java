package com.blitz.app.libraries.general.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by mrkcsc on 12/11/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGJson {

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
}
