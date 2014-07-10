package com.blitz.app.models.rest_objects;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Miguel Gaeta on 6/29/14.
 *
 * All API rest objects can potentially
 * contain an errors object.
 */
public class JsonObject {

    // Errors object.
    public Map<String, ArrayList<String>> errors;

    /**
     * Find out if any errors exist.
     *
     * @return True/false.
     */
    public boolean hasErrors() {

        return errors != null &&
               errors.size() > 0;
    }
}