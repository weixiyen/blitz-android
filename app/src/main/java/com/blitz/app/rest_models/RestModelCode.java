package com.blitz.app.rest_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallback;
import com.google.gson.JsonObject;

/**
 * Created by Miguel Gaeta on 6/28/14. Copyright 2014 Blitz Studios
 */
public class RestModelCode extends RestModel {

    // region Member Variables
    // =============================================================================================

    // User provided code.
    private String mValue;

    // Type of code provided.
    private String mCodeType;

    // endregion

    // region Public Methods
    // =============================================================================================

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

        RestAPICallback<JsonObject> operation =
                new RestAPICallback<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

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
        mRestAPI.code_post(body, operation);
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface RedeemCodeCallback {

        public void onRedeemCode();
    }

    // endregion
}