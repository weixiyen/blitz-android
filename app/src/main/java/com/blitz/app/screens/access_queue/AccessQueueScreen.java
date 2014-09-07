package com.blitz.app.screens.access_queue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.access_code.AccessCodeScreen;
import com.blitz.app.screens.sign_in.SignInScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperSpringsGroup;
import com.blitz.app.utilities.animations.AnimHelperSpringsPresets;
import com.blitz.app.utilities.animations.AnimHelperSpringsView;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelAccessQueue;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14. Copyright 2014 Blitz Studios
 */
public class AccessQueueScreen extends BaseActivity implements ViewModelAccessQueue.ViewModelAccessQueueCallbacks {

    // region Member Variables
    // =============================================================================================

    @InjectView(R.id.access_queue_promo_text)      View mQueuePromoText;
    @InjectView(R.id.access_queue_calls_to_action) View mQueueButtons;
    @InjectView(R.id.access_queue_position_info)   View mQueuePosInfo;
    @InjectView(R.id.access_queue_football_player) View mQueuePlayer;

    @InjectView(R.id.access_queue_people_ahead) TextView mQueuePeopleAhead;
    @InjectView(R.id.access_queue_people_behind) TextView mQueuePeopleBehind;

    // Page animations.
    private AnimHelperSpringsGroup mAnimations;

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
    public void onAccessGranted(boolean accessGranted) {

        if (accessGranted) {

            AuthHelper.grantAccess(this);
        }
    }

    // endregion

    // region Click Methods
    // =============================================================================================

    /**
     * Transition to access code screen.
     */
    @OnClick(R.id.access_queue_screen_have_code) @SuppressWarnings("unused")
    public void haveCode() {

        // Transition to access code screen.
        startActivity(new Intent(this, AccessCodeScreen.class));
    }

    /**
     * Transition to sign in screen.
     */
    @OnClick(R.id.access_queue_screen_have_account) @SuppressWarnings("unused")
    public void haveAccount() {

        // Transition to sign in screen.
        startActivity(new Intent(this, SignInScreen.class));
    }

    // endregion
}