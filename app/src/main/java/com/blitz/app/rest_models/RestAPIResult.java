package com.blitz.app.rest_models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nate on 9/18/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("unused")
class RestAPIResult<T> {

    // region Member Variables
    // ============================================================================================================

    // Results field.
    @SerializedName("results")
    private List<T> results;

    // Result field (only one result).
    @SerializedName("result")
    private T result;

    // Result can also be keyed as a user.
    @SerializedName("user")
    private T resultUser;

    @SerializedName("errors")
    private JsonObject errors;

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Fetch results object, some list of
     * rest object models.
     *
     * @return List of type.
     */
    public List<T> getResults() {

        return results;
    }

    /**
     * Fetch result object.
     *
     * @return Result of type.
     */
    public T getResult() {

        return result != null ? result : resultUser;
    }

    /**
     * Is there an error in the result.
     *
     * @return Yes or no.
     */
    public boolean hasErrors() {

        return errors != null && !errors.isJsonNull();
    }

    // endregion
}