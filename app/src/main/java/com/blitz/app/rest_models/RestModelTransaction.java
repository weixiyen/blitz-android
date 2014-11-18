package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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

        mRestAPI.transactions_get(Arrays.asList(userId), "user_id", "{\"created\": \"DESC\"}", limit,
                new RestAPICallback<>(activity, result -> callback.onSuccess(result.getResults()), null));

    }

    public static void postTransactionWithParams(Activity activity, String type, int amount, String nonce,
                                                 @NonNull RestResult<RestModelTransaction> callback) {


        JsonObject params = new JsonObject();
        params.addProperty("type", type);
        params.addProperty("amount", amount);
        params.addProperty("nonce", nonce);

        mRestAPI.transactions_post(params, new RestAPICallback<>(activity,
                (result) -> callback.onSuccess(result.getResult()), null));
    }

    public static void getTransactionToken(@NonNull RestAPICallback.OnSuccess<String> success,
                                           RestAPICallback.OnFailure failure) {

        mRestAPI.transaction_token_get(new Callback<RestAPIResult<RestModelTransaction>>() {
            @Override
            public void success(RestAPIResult<RestModelTransaction> result, Response response) {
                success.onSuccess(result.getResult().mToken);
            }

            @Override
            public void failure(RetrofitError error) {
                if (failure != null) {
                    failure.onFailure(error.getResponse(), error.isNetworkError());
                }
            }
        });
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


}
