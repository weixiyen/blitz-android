package com.blitz.app.utilities.rest;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nate on 9/18/14. Copyright 2014 Blitz Studios
 */
public class RestAPIResult<T> {

    // region Member Variables
    // =============================================================================================

    // Results field.
    @SerializedName("results") @SuppressWarnings("unused")
    private List<T> mResults;

    // Result field (only one result).
    @SerializedName("result") @SuppressWarnings("unused")
    private T mResult;

    // Result can also be keyed as a user.
    @SerializedName("user") @SuppressWarnings("unused")
    private T mResultUser;

    @SerializedName("errors") @SuppressWarnings("unused")
    private JsonObject mErrors;

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Fetch results object, some list of
     * rest object models.
     *
     * @return List of type.
     */
    public List<T> getResults() {

        return mResults;
    }

    /**
     * Fetch result object.
     *
     * @return Result of type.
     */
    public T getResult() {

        return mResult != null ? mResult : mResultUser;
    }

    /**
     * Is there an error in the result.
     *
     * @return Yes or no.
     */
    public boolean hasErrors() {

        return mErrors != null && !mErrors.isJsonNull();
    }

    // endregion
}