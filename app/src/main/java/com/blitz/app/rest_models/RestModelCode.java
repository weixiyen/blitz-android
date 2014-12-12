package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by Miguel Gaeta on 6/28/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class RestModelCode extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("code_type") @Getter private String codeType;

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Is this a valid code.
     *
     * @return True/false.
     */
    public boolean isValidCode() {

        return codeType != null &&
               codeType.equals("ACCESS_1");
    }

    /**
     * Redeem access code. Requires a code
     * value to be assigned.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    @SuppressWarnings("unused")
    public static void redeemCode(Activity activity,
                                  @NonNull String value,
                                  @NonNull RestResult<RestModelCode> callback) {

        // Construct POST body.
        JsonObject body = new JsonObject();
                   body.addProperty("value", value);

        // Make rest call for code.
        restAPI.code_post(body, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    // endregion
}