package com.blitz.app.screens.access_code;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.object_models.ObjectModelCode;
import com.blitz.app.utilities.rest.RestAPIOperation;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperGroup;
import com.blitz.app.utilities.animations.AnimHelperPresets;
import com.blitz.app.utilities.animations.AnimHelperView;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.keyboard.KeyboardUtility;

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
    @InjectView(R.id.access_code_code) EditText mCode;
    @InjectView(R.id.access_code_player) View mPlayer;
    @InjectView(R.id.access_code_top_container) View mAccessTopContainer;

    // Page animations.
    private AnimHelperGroup mAnimations;

    // View model.
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

        // Adjust and resize.
        setAdjustResize(true);

        // Create animation group.
        mAnimations = AnimHelperGroup.from(this);

        // Add a helper.
        mAnimations.createHelper(25, 7)
                .addHelperView(AnimHelperView.from(mContinue, AnimHelperPresets.SLIDE_UP))
                .addHelperView(AnimHelperView.from(mAccessTopContainer, AnimHelperPresets.SLIDE_DOWN));

        // Add a helper.
        mAnimations.createHelper(100, 20)
                .addHelperView(AnimHelperView.from(mPlayer, AnimHelperPresets.SLIDE_LEFT));

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

                    // Grant access.
                    AuthHelper.grantAccess(AccessCodeScreen.this);
                }
            }
        });
    }
}