package com.blitz.app.utilities.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Nate on 9/18/14. Copyright 2014 Blitz Studios
 */
public class RestAPIResult<T> {

    // region Member Variables
    // =============================================================================================

    // Results field.
    @SerializedName("results") @SuppressWarnings("unused") private List<T> mResults;

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

    // endregion
}