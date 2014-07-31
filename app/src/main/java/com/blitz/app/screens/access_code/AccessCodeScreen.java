package com.blitz.app.screens.access_code;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.models.objects.ObjectModelCode;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.screens.splash.SplashScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelper;
import com.blitz.app.utilities.animations.AnimHelperPresets;
import com.blitz.app.utilities.animations.AnimHelperView;
import com.blitz.app.utilities.app.AppDataObject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class AccessCodeScreen extends BaseActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.access_code_continue_with_code) View mContinue;
    @InjectView(R.id.access_code_code)  EditText mCode;
    @InjectView(R.id.access_code_player) View mPlayer;
    @InjectView(R.id.access_code_top_container) View mAccessTopContainer;

    private AnimHelper animsFromTop;
    private AnimHelper animsFromRight;

    private ObjectModelCode mObjectModelCode;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Setup the screen.
     *
     * @param savedInstanceState Instance parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This screen uses a slide animation.
        setCustomTransitions(CustomTransition.T_SLIDE_HORIZONTAL);

        animsFromTop = AnimHelper.from(this, 25, 7)
                .addHelperView(AnimHelperView.from(mContinue, AnimHelperPresets.SLIDE_UP))
                .addHelperView(AnimHelperView.from(mAccessTopContainer, AnimHelperPresets.SLIDE_DOWN));

        animsFromRight = AnimHelper.from(this, 100, 30)
                .addHelperView(AnimHelperView.from(mPlayer, AnimHelperPresets.SLIDE_LEFT));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Enable.
        animsFromTop.enable();
        animsFromRight.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Disable.
        animsFromTop.disable();
        animsFromRight.disable();
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.access_code_continue_with_code) @SuppressWarnings("unused")
    public void continueWithCode() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        if (mObjectModelCode == null) {
            mObjectModelCode = new ObjectModelCode();
        }

        // Provide code user inputted.
        mObjectModelCode.setValue(mCode.getText().toString());

        // Attempt to redeem it.
        mObjectModelCode.redeemCode(this, new ObjectModelCode.RedeemCodeCallback() {

            @Override
            public void onRedeemCode() {

                // If code is valid.
                if (mObjectModelCode.isValidCode()) {

                    // User now has access.
                    AppDataObject.hasAccess.set(true);

                    // Transition to splash screen, clear history.
                    startActivity(new Intent(AccessCodeScreen.this, SplashScreen.class), true);
                }
            }
        });
    }
}