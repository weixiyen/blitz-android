package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import lombok.Getter;

/**
 * Transactions Deposits/Withdrawals
 * Created by Nate on 11/10/14.
 */
@SuppressWarnings("UnusedDeclaration")
public class RestModelTransaction extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("user_id")       @Getter private String userId;
    @SerializedName("type")          @Getter private String type;
    @SerializedName("status")        @Getter private String status;
    @SerializedName("error_message") @Getter private String errorMessage;
    @SerializedName("token")         @Getter private String token;
    @SerializedName("amount")        @Getter private int amount;

    // endregion

    // region REST Calls
    // ============================================================================================================

    /**
     * Fetch associated transactions for a specified user.
     */
    public static void listTransactionsForUserId(Activity activity, String userId, int limit,
                                                 @NonNull RestResults<RestModelTransaction> callback) {

        restAPI.transactions_get(Arrays.asList(userId), "user_id", "{\"created\": \"DESC\"}", limit,
                new RestAPICallback<>(activity, result -> callback.onSuccess(result.getResults()), null));
    }

    /**
     * Post a transaction for this user.
     */
    public static void postTransactionWithParams(Activity activity, String type, int amount, String nonce,
                                                 @NonNull RestResult<RestModelTransaction> callback) {

        JsonObject body = new JsonObject();

        body.addProperty("type", type);
        body.addProperty("amount", amount);
        body.addProperty("nonce", nonce);

        restAPI.transactions_post(body, new RestAPICallback<>(activity,
                (result) -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Fetch users transaction token.
     */
    public static void getTransactionToken(Activity activity,
                                           @NonNull RestResult<RestModelTransaction> callback) {

        restAPI.transaction_token_get(new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Filter for specific error status.
     */
    public boolean isStatusError() {

        return "ERROR".equals(status);
    }

    // endregion
}