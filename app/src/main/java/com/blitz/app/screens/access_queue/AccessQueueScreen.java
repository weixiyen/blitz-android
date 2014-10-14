package com.blitz.app.screens.access_queue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.access_code.AccessCodeScreen;
import com.blitz.app.screens.sign_in.SignInScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperFade;
import com.blitz.app.utilities.animations.AnimHelperSpringsGroup;
import com.blitz.app.utilities.animations.AnimHelperSpringsPresets;
import com.blitz.app.utilities.animations.AnimHelperSpringsView;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelAccessQueue;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14. Copyright 2014 Blitz Studios
 */
public class AccessQueueScreen extends BaseActivity implements ViewModelAccessQueue.Callbacks {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.access_queue_promo_text)      View mQueuePromoText;
    @InjectView(R.id.access_queue_calls_to_action) View mQueueButtons;
    @InjectView(R.id.access_queue_position_info)   View mQueuePosInfo;
    @InjectView(R.id.access_queue_football_player) View mQueuePlayer;

    @InjectView(R.id.access_queue_people_ahead) TextView mQueuePeopleAhead;
    @InjectView(R.id.access_queue_people_behind) TextView mQueuePeopleBehind;

    @InjectView(R.id.access_queue_container_in_line)    View mQueueContainerInLine;
    @InjectView(R.id.access_queue_container_authorized) View mQueueContainerAuthorized;

    @InjectView(R.id.access_queue_have_account)  View mQueueHaveAccountText;
    @InjectView(R.id.access_queue_have_code) TextView mQueueHaveCode;

    // Page animations.
    private AnimHelperSpringsGroup mAnimations;
    private boolean mAnimationsComplete;

    // Track local access state.
    private Boolean mAccessGranted;

    // View model object.
    private ViewModelAccessQueue mViewModelAccessQueue;

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Setup spring animations.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create animation group.
        mAnimations = AnimHelperSpringsGroup.from(this);

        // Text and call to action.
        mAnimations.createHelper(25, 5)
                .addHelperView(AnimHelperSpringsView.from(mQueuePromoText, AnimHelperSpringsPresets.SLIDE_DOWN))
                .addHelperView(AnimHelperSpringsView.from(mQueueButtons, AnimHelperSpringsPresets.SLIDE_UP));

        // Queue container text.
        mAnimations.createHelper(25, 10)
                .addHelperView(AnimHelperSpringsView.from(mQueuePosInfo, AnimHelperSpringsPresets.SLIDE_RIGHT));

        // Football player guy.
        mAnimations.createHelper(100, 20)
                .addHelperView(AnimHelperSpringsView.from(mQueuePlayer, AnimHelperSpringsPresets.SLIDE_RIGHT));

        // Setup a completion listener for animations.
        mAnimations.setOnCompleteListener(new Runnable() {

            @Override
            public void run() {

                // Now done animating.
                mAnimationsComplete = true;

                // Setup UI if access received.
                if (mAccessGranted != null) {

                    setupUI(mAccessGranted);
                }
            }
        });

        // Hide parts of the ui at first.
        mQueueContainerInLine
                .setVisibility(View.GONE);
        mQueueContainerAuthorized
                .setVisibility(View.GONE);
        mQueueHaveAccountText
                .setVisibility(View.GONE);
        mQueueHaveCode
                .setVisibility(View.GONE);
    }

    /**
     * Enable animations when resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();

        mAnimations.enable();
    }

    /**
     * Disable animations when paused.
     */
    @Override
    protected void onPause() {
        super.onPause();

        mAnimations.disable();
    }

    /**
     * This method requests an instance of the view
     * model to operate on for lifecycle callbacks.
     *
     * @return Instantiated instance of the view model
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModelAccessQueue == null) {
            mViewModelAccessQueue = new ViewModelAccessQueue(this, this);
        }

        return mViewModelAccessQueue;
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Setup the UI based on whether or not
     * the user has access.
     *
     * @param accessGranted Is access granted.
     */
    private void setupUI(boolean accessGranted) {

        if (accessGranted) {

            // Update button text (hype train).
            mQueueHaveCode.setText(R.string.im_ready_let_me_in);

            // Show UI.
            AnimHelperFade.setVisibility(mQueueHaveCode, View.VISIBLE);
            AnimHelperFade.setVisibility(mQueueContainerAuthorized, View.VISIBLE);

            // User now has access.
            AppDataObject.hasAccess.set(true);
        } else {

            // Show UI.
            AnimHelperFade.setVisibility(mQueueHaveCode, View.VISIBLE);
            AnimHelperFade.setVisibility(mQueueHaveAccountText, View.VISIBLE);
            AnimHelperFade.setVisibility(mQueueContainerInLine, View.VISIBLE);
        }
    }

    // endregion

    // region View Model Callbacks
    // =============================================================================================

    /**
     * Update people in front of you.
     */
    @Override
    public void onPeopleAhead(int peopleAhead) {
        mQueuePeopleAhead.setText(Integer.toString(peopleAhead));
    }

    /**
     * Update people behind you.
     */
    @Override
    public void onPeopleBehind(int peopleBehind) {
        mQueuePeopleBehind.setText(Integer.toString(peopleBehind));
    }

    /**
     * Authorize user when access is
     * granted.
     */
    @Override
    public void onAccessGranted(final boolean accessGranted) {

        // Store access state.
        mAccessGranted = accessGranted;

        if (mAnimationsComplete) {

            // Setup immediately.
            setupUI(accessGranted);
        }
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    /**
     * Transition to access code screen.
     */
    @OnClick(R.id.access_queue_have_code) @SuppressWarnings("unused")
    public void haveCode() {

        if (mAccessGranted) {

            // User has now confirmed access state.
            AppDataObject.hasAccessConfirmed.set(true);

            // Enter the main app.
            AuthHelper.instance().tryEnterMainApp(this);

        } else {

            // Transition to access code screen.
            startActivity(new Intent(this, AccessCodeScreen.class));
        }
    }

    /**
     * Transition to sign in screen.
     */
    @OnClick(R.id.access_queue_have_account) @SuppressWarnings("unused")
    public void haveAccount() {

        // Transition to sign in screen.
        startActivity(new Intent(this, SignInScreen.class));
    }

    // endregion
}