package com.blitz.app.models.objects;

import com.blitz.app.models.operation.ModelOperationInterface;
import com.blitz.app.models.rest.BlitzRestAPI;
import com.blitz.app.models.rest.BlitzRestAPIClient;
import com.blitz.app.models.rest.BlitzRestAPIObject;
import com.blitz.app.models.rest.BlitzRestAPICallback;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class ObjectModelCode {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // User provided code.
    private String mValue;

    // Rest api code object.
    private BlitzRestAPIObject mCode;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Empty constructor disallowed.
     */
    public ObjectModelCode() {
        mCode = new BlitzRestAPIObject();
    }

    public void setValue(String value) {
        mValue = value;
    }

    private BlitzRestAPI.Code getCode() {
        return (BlitzRestAPI.Code)mCode.mApiObject;
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Is this a valid code.
     *
     * @return True/false.
     */
    public boolean isValidCode() {

        return getCode()                  != null &&
               getCode().result           != null &&
               getCode().result.code_type != null &&
               getCode().result.code_type.equals("ACCESS_1");
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
        BlitzRestAPIClient.getAPI().code(body, new BlitzRestAPICallback<BlitzRestAPI.Code>(mCode, operation));
    }
}