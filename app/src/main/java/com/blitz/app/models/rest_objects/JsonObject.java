package com.blitz.app.models.rest_objects;

import java.util.ArrayList;

/**
 * Created by Miguel Gaeta on 6/29/14.
 *
 * All API rest objects can potentially
 * contain an errors object.
 */
public class JsonObject {

    /**
     * Contains a list of strings
     * pertaining to the error.
     */
    public static class Errors {

        // Error string payload.
        public ArrayList<String> value;
    }

    // Errors object.
    public Errors errors;

    /**
     * Find out if any errors exist.
     *
     * @return True/false.
     */
    public boolean hasErrors() {

        return errors       != null &&
                errors.value != null;
    }
}