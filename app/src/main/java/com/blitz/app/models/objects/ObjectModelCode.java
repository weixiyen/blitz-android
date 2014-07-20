package com.blitz.app.models.objects;

import android.app.Activity;

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

    // Type of code provided.
    private String mCodeType;

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

        return mCodeType != null &&
               mCodeType.equals("ACCESS_1");
    }

    /**
     * Redeem access code. Requires a code
     * value to be assigned.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void redeemCode(Activity activity, final RedeemCodeCallback callback) {

        // Define operation.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success() {

                // Fetch resulting object.
                JsonObjectCode jsonObject = getJsonObject(JsonObjectCode.class);

                if (jsonObject                  != null &&
                    jsonObject.result           != null &&
                    jsonObject.result.code_type != null) {

                    // Assign result.
                    mCodeType = jsonObject.result.code_type;
                }

                // Code redeemed.
                callback.onRedeemCode();
            }
        };

        // Construct POST body.
        JsonObjectCode.Body body = new JsonObjectCode.Body(mValue);

        // Make rest call for code.
        RestAPIClient.getAPI().code(body, new RestAPICallback<JsonObjectCode>(mRestApiObject, operation));
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    public interface RedeemCodeCallback {

        public void onRedeemCode();
    }
}