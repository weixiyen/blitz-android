package com.blitz.app.screens.access_queue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.view_models.ViewModelAccessQueue;
import com.blitz.app.screens.access_code.AccessCodeScreen;
import com.blitz.app.screens.sign_in.SignInScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperGroup;
import com.blitz.app.utilities.animations.AnimHelperPresets;
import com.blitz.app.utilities.animations.AnimHelperView;
import com.blitz.app.utilities.authentication.AuthHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class AccessQueueScreen extends BaseActivity implements ViewModelAccessQueue.ViewModelAccessQueueCallbacks {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.access_queue_promo_text)      View mQueuePromoText;
    @InjectView(R.id.access_queue_calls_to_action) View mQueueButtons;
    @InjectView(R.id.access_queue_position_info)   View mQueuePosInfo;
    @InjectView(R.id.access_queue_football_player) View mQueuePlayer;

    @InjectView(R.id.access_queue_people_ahead) TextView mQueuePeopleAhead;
    @InjectView(R.id.access_queue_people_behind) TextView mQueuePeopleBehind;

    // Page animations.
    private AnimHelperGroup mAnimations;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the view model.
        setViewModel(new ViewModelAccessQueue(), savedInstanceState);

        // Create animation group.
        mAnimations = AnimHelperGroup.from(this);

        // Text and call to action.
        mAnimations.createHelper(25, 5)
                .addHelperView(AnimHelperView.from(mQueuePromoText, AnimHelperPresets.SLIDE_DOWN))
                .addHelperView(AnimHelperView.from(mQueueButtons,   AnimHelperPresets.SLIDE_UP));

        // Queue container text.
        mAnimations.createHelper(25, 10)
                .addHelperView(AnimHelperView.from(mQueuePosInfo, AnimHelperPresets.SLIDE_RIGHT));

        // Football player guy.
        mAnimations.createHelper(100, 20)
                .addHelperView(AnimHelperView.from(mQueuePlayer, AnimHelperPresets.SLIDE_RIGHT));
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
    // View Model Callbacks
    //==============================================================================================

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

    //==============================================================================================
    // Click Methods
    //==============================================================================================

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
}