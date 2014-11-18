package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestModelTransaction;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.rest_models.RestResult;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.logging.LogHelper;

/**
 * Deposit view model.
 *
 * Created by Nate on 11/15/14.
 */
public class ViewModelDeposit extends ViewModel {

    private RestModelUser mUser;
    private int mDepositAmountInCents;
    private String mTransactionToken;
    boolean mPaymentComplete;

    @SuppressWarnings("unused")
    private String mErrorMessage;

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.\
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelDeposit(BaseActivity activity, Callbacks callbacks) {
        super(activity, callbacks);
    }

    @Override
    public void initialize() {

        // initialize user
        RestModelUser.getUser(mActivity, AuthHelper.instance().getUserId(), new RestResult<RestModelUser>() {
            @Override
            public void onSuccess(RestModelUser object) {
                mUser = object;
                onInitialized();
            }
        }, true);

        RestModelTransaction.getTransactionToken(
                token -> {
                    mTransactionToken = token;
                    onInitialized();
                }, null);

        // TODO: initialize location manager
    }

    private void onInitialized() {
        if(mUser != null && mTransactionToken != null) { // finished initializing

            if (getCallbacks(Callbacks.class) != null) {
                getCallbacks(Callbacks.class).consume();
            }
        }
    }

    @SuppressWarnings("unused")
    public void payDepositWithNonce(String nonce) {

        RestModelTransaction.postTransactionWithParams(mActivity, "DEPOSIT", mDepositAmountInCents,
                nonce, new RestResult<RestModelTransaction>() {
            @Override
            public void onSuccess(RestModelTransaction object) {
                mPaymentComplete = true;

                LogHelper.log("PAY DEPOSIT WITH NONCE PAID");
            }
        });
    }

    public void setDepositAmountInCents(int cents) {
        mDepositAmountInCents = cents;
    }

    public String getCurrentBalance() {
        return stringWithDollarFormat(mUser.getCash());
    }

    @SuppressWarnings("unused")
    public String getDepositAmount() {
        return stringWithDollarFormat(mDepositAmountInCents);
    }

    public String getTransactionToken() {
        return mTransactionToken;
    }

    public String getAmountAfterDeposit() {
        return stringWithDollarFormat(mUser.getCash() + mDepositAmountInCents);
    }

    public boolean isReady() {
        return mUser != null && mTransactionToken != null;
    }

    private static String stringWithDollarFormat(int cents) {
        return String.format("$%.02f", cents / 100.0);
    }

    public interface Callbacks extends ViewModel.Callbacks {
        void consume();
    }
}
