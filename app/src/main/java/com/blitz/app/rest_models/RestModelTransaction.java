package com.blitz.app.rest_models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

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

    public static void listTransactionsForUserId(String userId, int limit, RestAPICallback<RestAPIResult<RestModelTransaction>> callback) {
        mRestAPI.transactions_get(Arrays.asList(userId), "user_id", "{\"created\": \"DESC\"}", 150, callback);
    }


}
