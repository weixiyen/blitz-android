package com.blitz.app.screens.payments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.dialogs.error.DialogErrorSingleton;
import com.blitz.app.dialogs.info.DialogInfo;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.logging.LogHelper;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelDeposit;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;


/**
 * Screen for depositing money.
 *
 * Created by Nate on 11/5/14.
 */
public class DepositScreen extends BaseActivity implements ViewModelDeposit.Callbacks {

    // arbitrary code to identity Braintree payment activity
    private static final int PAYMENT_REQUEST_CODE = 1111;

    private ViewModelDeposit mViewModel;
    private DialogInfo mDepositResultDialog;

    @InjectViews({
            R.id.deposit_amount_10,
            R.id.deposit_amount_25,
            R.id.deposit_amount_50,
            R.id.deposit_amount_100

    }) List<TextView> mDepositAmounts;

    @InjectView(R.id.deposit_amount) TextView mDepositAmount;

    @InjectView(R.id.deposit_current_balance) TextView mCurrentBalance;
    @InjectView(R.id.deposit_new_balance) TextView mNewBalance;

    @InjectView(R.id.deposit_button) View mDepositButton;

    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModel == null) {
            mViewModel = new ViewModelDeposit(this, this);
        }

        return mViewModel;
    }

    /**
     * Run custom transitions if needed.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Modal presentation style.
        setCustomTransitions(CustomTransition.T_SLIDE_VERTICAL);
    }

    @OnClick(R.id.deposit_button) @SuppressWarnings("unused")
    public void onDepositClicked(View v) {
        if(mViewModel != null && mViewModel.isReady()) {
            Intent intent = new Intent(DepositScreen.this, BraintreePaymentActivity.class);
            intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, mViewModel.getTransactionToken());
            startActivityForResult(intent, PAYMENT_REQUEST_CODE);
        }
    }

    @OnClick({
            R.id.deposit_amount_10,
            R.id.deposit_amount_25,
            R.id.deposit_amount_50,
            R.id.deposit_amount_100}) @SuppressWarnings("unused")
    public void onDepositAmountClicked(TextView depositAmount) {

        ButterKnife.apply(mDepositAmounts, (view, index) -> {

            view.setTextColor(getResources().getColor(R.color.text_color_dark));
            view.setBackgroundResource(R.color.background_transparent);
        });

        // Set selected button state.
        depositAmount.setTextColor(getResources().getColor(R.color.active_blue));
        depositAmount.setBackgroundResource(R.drawable.drawable_payments_inner_bg);

        // Set currently selected amount text.
        mDepositAmount.setText(depositAmount.getText());

        // Set the ViewModel deposit amount in cents.
        mViewModel.setDepositAmountInCents
                (Integer.valueOf((String)depositAmount.getTag()));

        // Set current balance text.
        mCurrentBalance.setText(mViewModel.getCurrentBalance());
        // Set new balance (after deposit) text.
        mNewBalance.setText(mViewModel.getAmountAfterDeposit());

        mDepositButton.setClickable(true);
    }

    @Override
    public void consume() {
        mDepositAmounts.get(0).performClick();
    }

    @Override
    public void onDepositSuccess() {

        showInfoDialog(R.string.deposit_success_dialog_text);
    }

    @Override
    public void onDepositFailure() {

        showInfoDialog(R.string.deposit_failure_dialog_text);
    }

    private void showInfoDialog(int textResourceId) {

        if (mDepositResultDialog != null) {
            mDepositResultDialog.hide(null);
        }

        mDepositResultDialog = new DialogInfo(this);
        mDepositResultDialog.setInfoText(textResourceId);
        mDepositResultDialog.setDismissible(true);
        mDepositResultDialog.show(true);
    }

    /**
     * Handle result from the Braintree payments screen.
     * See https://developers.braintreepayments.com/android+python/sdk/client/drop-in
     *
     * TODO: test payment sandbox with special values to trigger error handling
     * https://developers.braintreepayments.com/android+python/reference/general/testing
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYMENT_REQUEST_CODE) {
            switch (resultCode) {
                case BraintreePaymentActivity.RESULT_OK:
                    String paymentMethodNonce = data
                            .getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                    mViewModel.payDepositWithNonce(paymentMethodNonce);
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    DialogErrorSingleton.showGeneric();
                    Object error = data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE);
                    if (error instanceof Throwable) {
                        LogHelper.log(((Throwable) error).getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
