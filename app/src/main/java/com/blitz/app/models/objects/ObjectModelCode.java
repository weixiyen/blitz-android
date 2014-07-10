package com.blitz.app.models.objects;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest_objects.JsonObjectCode;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class ObjectModelCode extends ObjectModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // User provided code.
    private String mValue;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    private JsonObjectCode getCode() {

        return mRestApiObject.getJsonObject(JsonObjectCode.class);
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Set code value.
     *
     * @param value Code value.
     */
    public void setValue(String value) {

        mValue = value;
    }

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
    public void redeemCode(RestAPIOperation operation) {

        // Construct POST body.
        JsonObjectCode.Body body = new JsonObjectCode.Body(mValue);

        // Make rest call for code.
        RestAPIClient.getAPI().code(body, new RestAPICallback<JsonObjectCode>(mRestApiObject, operation));
    }
}