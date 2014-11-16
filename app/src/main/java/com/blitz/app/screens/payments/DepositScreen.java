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
public class DepositScreen extends BaseActivity implements ViewModel.Callbacks {

    private ViewModelDeposit mViewModel;

    @InjectViews({
            R.id.deposit_amount_10,
            R.id.deposit_amount_25,
            R.id.deposit_amount_50,
            R.id.deposit_amount_100

    }) List<TextView> mDepositAmounts;

    @InjectView(R.id.deposit_amount) TextView mDepositAmount;

    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModel == null) {
            mViewModel = new ViewModelDeposit(this, null);
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

        // Select the first deposit amount.
        mDepositAmounts.get(0).performClick();
    }

    @OnClick(R.id.deposit_button) @SuppressWarnings("unused")
    public void onDepositClicked(View v) {
        Intent intent = new Intent(DepositScreen.this, BraintreePaymentActivity.class);
        intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, "eyJ2ZXJzaW9uIjoxLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiIyYjAyMGM2MmM1Mzk0MWVhOTdlMjdlNzk4N2Y2ODFhOWE1NjE3NWU4ZTFkN2VlZGMxZTNmZjI0Yzk1MjQ5NTI4fGNyZWF0ZWRfYXQ9MjAxNC0xMS0wNlQwNzowMDoxMC4xMjcxNjY2NzkrMDAwMFx1MDAyNm1lcmNoYW50X2lkPWRjcHNweTJicndkanIzcW5cdTAwMjZwdWJsaWNfa2V5PTl3d3J6cWszdnIzdDRuYzgiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvZGNwc3B5MmJyd2RqcjNxbi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwicGF5bWVudEFwcHMiOltdLCJjbGllbnRBcGlVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvZGNwc3B5MmJyd2RqcjNxbi9jbGllbnRfYXBpIiwiYXNzZXRzVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhdXRoVXJsIjoiaHR0cHM6Ly9hdXRoLnZlbm1vLnNhbmRib3guYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhbmFseXRpY3MiOnsidXJsIjoiaHR0cHM6Ly9jbGllbnQtYW5hbHl0aWNzLnNhbmRib3guYnJhaW50cmVlZ2F0ZXdheS5jb20ifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6ZmFsc2UsInBheXBhbEVuYWJsZWQiOnRydWUsInBheXBhbCI6eyJkaXNwbGF5TmFtZSI6IkFjbWUgV2lkZ2V0cywgTHRkLiAoU2FuZGJveCkiLCJjbGllbnRJZCI6bnVsbCwicHJpdmFjeVVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS9wcCIsInVzZXJBZ3JlZW1lbnRVcmwiOiJodHRwOi8vZXhhbXBsZS5jb20vdG9zIiwiYmFzZVVybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXNzZXRzVXJsIjoiaHR0cHM6Ly9jaGVja291dC5wYXlwYWwuY29tIiwiZGlyZWN0QmFzZVVybCI6bnVsbCwiYWxsb3dIdHRwIjp0cnVlLCJlbnZpcm9ubWVudE5vTmV0d29yayI6dHJ1ZSwiZW52aXJvbm1lbnQiOiJvZmZsaW5lIiwibWVyY2hhbnRBY2NvdW50SWQiOiJzdGNoMm5mZGZ3c3p5dHc1IiwiY3VycmVuY3lJc29Db2RlIjoiVVNEIn0sImNvaW5iYXNlRW5hYmxlZCI6ZmFsc2V9");
        startActivityForResult(intent, 100);
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

        // Set the deposit amount in cents.
        mViewModel.setDepositAmountInCents
                (Integer.valueOf((String)depositAmount.getTag()));
    }
}