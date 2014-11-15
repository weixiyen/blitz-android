package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Map;

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

    public static void listTransactionsForUserId(Activity activity, String userId, int limit,
                                                 @NonNull RestResults<RestModelTransaction> callback) {

        mRestAPI.transactions_get(Arrays.asList(userId), "user_id", "{\"created\": \"DESC\"}", 150,
                new RestAPICallback<>(activity, result -> callback.onSuccess(result.getResults()), null));

    }

    public static void postTransactionWithParams(Activity activity, Map<String, String> params,
                                                 @NonNull RestResult<RestModelTransaction> callback) {

        mRestAPI.transaction_post(params, new RestAPICallback<>(activity,
                transaction -> callback.onSuccess(transaction.getResult()), null));
    }

    public int getAmount() {
        return mAmount;
    }

    public String getType() {
        return mType;
    }

    public boolean hasErrors() {
       return "ERROR".equals(mStatus);
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }


}
