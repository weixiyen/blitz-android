package com.blitz.app.screens.access_code;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.object_models.RestModelCode;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperSpringsGroup;
import com.blitz.app.utilities.animations.AnimHelperSpringsPresets;
import com.blitz.app.utilities.animations.AnimHelperSpringsView;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.keyboard.KeyboardUtility;
import com.blitz.app.utilities.rest.RestAPICallback;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14. Copyright 2014 Blitz Studios
 */
public class AccessCodeScreen extends BaseActivity {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.access_code_continue_with_code) View mContinue;
    @InjectView(R.id.access_code_code) EditText mCode;
    @InjectView(R.id.access_code_player) View mPlayer;
    @InjectView(R.id.access_code_top_container) View mAccessTopContainer;

    // Page animations.
    private AnimHelperSpringsGroup mAnimations;

    // Model.
    private RestModelCode mObjectModelCode;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

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

        // Adjust and resize.
        setAdjustResize(true);

        // Create animation group.
        mAnimations = AnimHelperSpringsGroup.from(this);

        // Add a helper.
        mAnimations.createHelper(25, 7)
                .addHelperView(AnimHelperSpringsView.from(mContinue, AnimHelperSpringsPresets.SLIDE_UP))
                .addHelperView(AnimHelperSpringsView.from(mAccessTopContainer, AnimHelperSpringsPresets.SLIDE_DOWN));

        // Add a helper.
        mAnimations.createHelper(100, 20)
                .addHelperView(AnimHelperSpringsView.from(mPlayer, AnimHelperSpringsPresets.SLIDE_LEFT));

        // Open the keyboard when done.
        mAnimations.setOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                KeyboardUtility.showKeyboard(AccessCodeScreen.this, mCode);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAnimations.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mAnimations.disable();
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    @OnClick(R.id.access_code_continue_with_code) @SuppressWarnings("unused")
    public void continueWithCode() {

        if (RestAPICallback.shouldThrottle()) {
            return;
        }

        if (mObjectModelCode == null) {
            mObjectModelCode = new RestModelCode();
        }

        // Provide code user inputted.
        mObjectModelCode.setValue(mCode.getText().toString());

        // Attempt to redeem it.
        mObjectModelCode.redeemCode(this, new RestModelCode.RedeemCodeCallback() {

            @Override
            public void onRedeemCode() {

                // If code is valid.
                if (mObjectModelCode.isValidCode()) {

                    // Grant access.
                    AuthHelper.instance().grantAccess(AccessCodeScreen.this);
                }
            }
        });
    }

    // endregion
}