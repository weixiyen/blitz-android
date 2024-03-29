package com.blitz.app.screens.access_code;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestModel;
import com.blitz.app.rest_models.RestModelCode;
import com.blitz.app.rest_models.RestResult;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperSpringsGroup;
import com.blitz.app.utilities.animations.AnimHelperSpringsPresets;
import com.blitz.app.utilities.animations.AnimHelperSpringsView;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.keyboard.KeyboardUtility;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14. Copyright 2014 Blitz Studios
 */
public class AccessCodeScreen extends BaseActivity {

    // region Member Variables
    // ============================================================================================================

    @InjectView(R.id.access_code_continue_with_code) View mContinue;
    @InjectView(R.id.access_code_code) EditText mCode;
    @InjectView(R.id.access_code_player) View mPlayer;
    @InjectView(R.id.access_code_top_container) View mAccessTopContainer;

    // Page animations.
    private AnimHelperSpringsGroup mAnimations;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

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
        mAnimations.setOnCompleteListener(() -> KeyboardUtility.showKeyboard(AccessCodeScreen.this, mCode));
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
    // ============================================================================================================

    @OnClick(R.id.access_code_continue_with_code) @SuppressWarnings("unused")
    public void continueWithCode() {

        if (RestModel.shouldThrottle()) {
            return;
        }

        // Attempt to redeem access code.
        RestModelCode.redeemCode(this, mCode.getText().toString(), new RestResult<RestModelCode>() {

            @Override
            public void onSuccess(RestModelCode object) {

                // If code is valid.
                if (object.isValidCode()) {

                    // User has now confirmed access state.
                    AppDataObject.hasAccessConfirmed.set(true);
                    AppDataObject.hasAccess.set(true);

                    // Enter the main app.
                    AuthHelper.instance().tryEnterMainApp(AccessCodeScreen.this);
                }
            }
        });
    }

    // endregion
}