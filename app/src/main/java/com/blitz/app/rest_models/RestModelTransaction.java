package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Transactions Deposits/Withdrawals
 * Created by Nate on 11/10/14.
 */
public class RestModelTransaction extends RestModel {

    @SerializedName("user_id")
    @SuppressWarnings("unused")
    private String mUserId;
    @SerializedName("type")
    @SuppressWarnings("unused")
    private String mType;
    @SerializedName("status")
    @SuppressWarnings("unused")
    private String mStatus;
    @SerializedName("amount")
    @SuppressWarnings("unused")
    private int mAmount;
    @SerializedName("error_message")
    @SuppressWarnings("unused")
    private String mErrorMessage;
    @SerializedName("token")
    @SuppressWarnings("unused")
    private String mToken;


    public static void listTransactionsForUserId(Activity activity, String userId, int limit,
                                                 @NonNull RestResults<RestModelTransaction> callback) {

        restAPI.transactions_get(Arrays.asList(userId), "user_id", "{\"created\": \"DESC\"}", limit,
                new RestAPICallback<>(activity, result -> callback.onSuccess(result.getResults()), null));

    }

    public static void postTransactionWithParams(Activity activity, String type, int amount, String nonce,
                                                 @NonNull RestResult<RestModelTransaction> callback) {


        JsonObject params = new JsonObject();
        params.addProperty("type", type);
        params.addProperty("amount", amount);
        params.addProperty("nonce", nonce);

        restAPI.transactions_post(params, new RestAPICallback<>(activity,
                (result) -> callback.onSuccess(result.getResult()), null));
    }

    public static void getTransactionToken(Activity activity,
                                           @NonNull RestResult<RestModelTransaction> callback) {

        mRestAPI.transaction_token_get(new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    public int getAmount() {
        return mAmount;
    }

    public String getType() {
        return mType;
    }

    // TODO: add explicit error handling for errors returned from the server.
    @SuppressWarnings("unused")
    public boolean hasErrors() {
       return "ERROR".equals(mStatus);
    }

    // TODO: add explicit error handling for errors returned from the server.
    @SuppressWarnings("unused")
    public String getErrorMessage() {
        return mErrorMessage;
    }

    public String getToken() {
        return mToken;
    }
}
