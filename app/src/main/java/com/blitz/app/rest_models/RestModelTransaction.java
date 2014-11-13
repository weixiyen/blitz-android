package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

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

    public static void listTransactionsForUserId(Activity activity, String userId, int limit,
                                                 @NonNull RestResults<RestModelTransaction> callback) {

        mRestAPI.transactions_get(Arrays.asList(userId), "user_id", "{\"created\": \"DESC\"}", 150,
                new RestAPICallback<>(activity, result -> callback.onSuccess(result.getResults()), null));

    }

    public int getAmount() {
        return mAmount;
    }

    public String getType() {
        return mType;
    }


}
