package com.blitz.app.rest_models;

import com.blitz.app.utilities.rest.RestAPI;
import com.blitz.app.utilities.rest.RestAPIClient;

/**
 * Created by mrkcsc on 9/7/14. Copyright 2014 Blitz Studios
 */
public class RestModel {

    // region Member Variables
    // ============================================================================================================

    // Object models have a final static instance of the rest API.
    protected static final RestAPI mRestAPI = RestAPIClient.getAPI();

    // endregion
}