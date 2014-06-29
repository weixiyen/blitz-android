package com.blitz.app.models.objects;

import com.blitz.app.models.operation.ModelOperationInterface;
import com.blitz.app.models.rest.RestAPI;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIObject;
import com.blitz.app.models.rest.RestAPICallback;

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
    private RestAPIObject mCode;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Empty constructor disallowed.
     */
    public ObjectModelCode() {
        mCode = new RestAPIObject();
    }

    public void setValue(String value) {
        mValue = value;
    }

    private RestAPI.Code getCode() {
        return (RestAPI.Code)mCode.mApiObject;
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
        RestAPI.Code.Body body = new RestAPI.Code.Body();

        body.value = mValue;

        // Make rest call for code.
        RestAPIClient.getAPI().code(body, new RestAPICallback<RestAPI.Code>(mCode, operation));
    }
}