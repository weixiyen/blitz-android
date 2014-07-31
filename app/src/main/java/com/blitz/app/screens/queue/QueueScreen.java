package com.blitz.app.screens.queue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.screens.access_code.AccessCodeScreen;
import com.blitz.app.screens.sign_in.SignInScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelperGroup;
import com.blitz.app.utilities.animations.AnimHelperPresets;
import com.blitz.app.utilities.animations.AnimHelperView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class QueueScreen extends BaseActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.queue_promo_text)      View mQueuePromoText;
    @InjectView(R.id.queue_calls_to_action) View mQueueButtons;
    @InjectView(R.id.queue_position_info)   View mQueuePosInfo;
    @InjectView(R.id.queue_football_player) View mQueuePlayer;

    // Page animations.
    private AnimHelperGroup mAnimations;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        mAnimations.createHelper(100, 30)
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
    // Click Methods
    //==============================================================================================

    /**
     * Transition to access code screen.
     */
    @OnClick(R.id.queue_screen_have_code) @SuppressWarnings("unused")
    public void haveCode() {

        // Transition to access code screen.
        startActivity(new Intent(this, AccessCodeScreen.class));
    }

    /**
     * Transition to sign in screen.
     */
    @OnClick(R.id.queue_screen_have_account) @SuppressWarnings("unused")
    public void haveAccount() {

        // Transition to sign in screen.
        startActivity(new Intent(this, SignInScreen.class));
    }
}