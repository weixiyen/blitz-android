package com.blitz.app.screens.queue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.screens.access_code.AccessCodeScreen;
import com.blitz.app.screens.sign_in.SignInScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimHelper;
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
    private AnimHelper animTextCallToActions;
    private AnimHelper animQueueContainer;
    private AnimHelper animFootballPlayer;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Text and call to action.
        animTextCallToActions = AnimHelper.from(this, 25, 5)
                .addHelperView(AnimHelperView.from(mQueuePromoText, AnimHelperPresets.SLIDE_DOWN))
                .addHelperView(AnimHelperView.from(mQueueButtons, AnimHelperPresets.SLIDE_UP));

        // Queue container text.
        animQueueContainer = AnimHelper.from(this, 25, 10)
                .addHelperView(AnimHelperView.from(mQueuePosInfo, AnimHelperPresets.SLIDE_RIGHT));

        // Football player guy.
        animFootballPlayer = AnimHelper.from(this, 100, 30)
                .addHelperView(AnimHelperView.from(mQueuePlayer, AnimHelperPresets.SLIDE_RIGHT));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Enable.
        animTextCallToActions.enable();
        animQueueContainer.enable();
        animFootballPlayer.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Disable.
        animTextCallToActions.disable();
        animQueueContainer.disable();
        animFootballPlayer.disable();
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