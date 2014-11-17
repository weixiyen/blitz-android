package com.blitz.app.screens.payments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
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

    private ViewModelDeposit mViewModel;

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
            startActivityForResult(intent, 100);
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
}
