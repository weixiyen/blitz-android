package com.blitz.app.libraries.general.json;

import android.support.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by mrkcsc on 12/11/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGJsonObjectBuilder {

    private JsonObject jsonObject = new JsonObject();

    private MGJsonObjectBuilder() {

    }

    /**
     * Create new instance.
     */
    public static MGJsonObjectBuilder create() {

        return new MGJsonObjectBuilder();
    }

    /**
     * Add a json element.
     */
    public MGJsonObjectBuilder add(@NonNull String property,
                            @NonNull JsonElement value) {

        jsonObject.add(property, value);

        return this;
    }

    /**
     * Add a boolean property.
     */
    public MGJsonObjectBuilder addProperty(@NonNull String property,
                                    @NonNull Boolean value) {

        jsonObject.addProperty(property, value);

        return this;
    }

    /**
     * Add a string property.
     */
    public MGJsonObjectBuilder addProperty(@NonNull String property,
                                         @NonNull String value) {

        jsonObject.addProperty(property, value);

        return this;
    }

    /**
     * Add a character property.
     */
    public MGJsonObjectBuilder addProperty(@NonNull String property,
                                    @NonNull Character value) {

        jsonObject.addProperty(property, value);

        return this;
    }

    /**
     * Add a number property.
     */
    public MGJsonObjectBuilder addProperty(@NonNull String property,
                                    @NonNull Number value) {

        jsonObject.addProperty(property, value);

        return this;
    }

    /**
     * Fetch the inner json object.
     */
    public JsonObject get() {

        return jsonObject;
    }
}