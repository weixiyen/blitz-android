package com.blitz.app.models.objects;

import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIObject;
import com.blitz.app.models.rest_objects.JsonObjectCode;

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

    private JsonObjectCode getCode() {

        return mCode.getJsonObject(JsonObjectCode.class);
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
    public void redeemCode(RestAPIOperation operation) {

        // Construct POST body.
        JsonObjectCode.Body body = new JsonObjectCode.Body(mValue);

        // Make rest call for code.
        RestAPIClient.getAPI().code(body, new RestAPICallback<JsonObjectCode>(mCode, operation));
    }
}