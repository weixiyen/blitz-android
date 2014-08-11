package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIClient;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.google.gson.JsonObject;

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
                JsonObject jsonObject = getJsonObject();

                if (jsonObject != null) {

                    // Assign result.
                    mCodeType = jsonObject.getAsJsonObject("result")
                            .get("code_type").getAsString();
                }

                // Code redeemed.
                callback.onRedeemCode();
            }
        };

        // Construct POST body.
        JsonObject body = new JsonObject();
                   body.addProperty("value", mValue);

        // Make rest call for code.
        RestAPIClient.getAPI().code_post(body, RestAPICallback.create(mRestApiObject, operation));
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    public interface RedeemCodeCallback {

        public void onRedeemCode();
    }
}