package com.blitz.app.models.objects;

import com.blitz.app.models.operation.ModelOperationInterface;
import com.blitz.app.models.rest.BlitzRestAPI;
import com.blitz.app.models.rest.BlitzRestAPIClient;
import com.blitz.app.models.rest.BlitzRestApiCallback;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class ObjectModelCode extends ObjectModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // User provided code.
    private String mValue;

    // Rest api code object.
    private BlitzRestAPI.Code mCode;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Empty constructor disallowed.
     */
    @SuppressWarnings("unused")
    private ObjectModelCode() {

    }

    public ObjectModelCode(String value) {
        mValue = value;
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    public BlitzRestAPI.Code getApiObject() {
        return mCode;
    }

    public void setApiObject(BlitzRestAPI.Code apiObject) {
        mCode = apiObject;
    }

    /**
     * Is this a valid code.
     *
     * @return True/false.
     */
    public boolean isValidCode() {

        return mCode                  != null &&
               mCode.result           != null &&
               mCode.result.code_type != null &&
               mCode.result.code_type.equals("ACCESS_1");
    }

    /**
     * Redeem access code.
     *
     * @param operation Operation callback model.
     */
    public void redeemCode(final ModelOperationInterface operation) {

        // Construct POST body.
        BlitzRestAPI.Code.Body body = new BlitzRestAPI.Code.Body();

        body.value = mValue;

        // Make rest call for code.
        BlitzRestAPIClient.getAPI().code(body, new BlitzRestApiCallback<BlitzRestAPI.Code>(this, operation));
    }
}