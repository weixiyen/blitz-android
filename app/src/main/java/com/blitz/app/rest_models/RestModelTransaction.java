package com.blitz.app.rest_models;

import com.blitz.app.utilities.rest.RestAPIResult;
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
    private String mUserId;
    @SerializedName("type")
    private String mType;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("amount")
    private int mAmount;
    @SerializedName("error_message")
    private String mErrorMessage;

    public static void listTransactionsForUserId(String userId, int limit, RestModelCallbacks<RestModelTransaction> callback) {
        mRestAPI.transactions_get(Arrays.asList(userId), "user_id", "{\"created\": \"DESC\"}", 150, new Callback<RestAPIResult<RestModelTransaction>>() {

            @Override
            public void success(RestAPIResult<RestModelTransaction> restModelTransactionRestAPIResult, Response response) {
                callback.onSuccess(restModelTransactionRestAPIResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFailure();
            }
        });
    }

    public int getAmount() {
        return mAmount;
    }

    public String getType() {
        return mType;
    }


}
