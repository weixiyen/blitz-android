package com.blitz.app.screens.queue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.screens.access_code.AccessCodeScreen;
import com.blitz.app.screens.sign_in.SignInScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimationHelper;
import com.blitz.app.utilities.animations.AnimationHelperView;

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
    @InjectView(R.id.queue_calls_to_action) View mQueueCallsToAction;
    @InjectView(R.id.queue_position_info)   View mQueuePositionInfo;
    @InjectView(R.id.queue_football_player) View mQueueFootballPlayer;

    // Page animations.
    private AnimationHelper animTextCallToActions;
    private AnimationHelper animQueueContainer;
    private AnimationHelper animFootballPlayer;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AnimationHelperView
            aQueuePromoText = new AnimationHelperView(mQueuePromoText);
            aQueuePromoText.setTranslationYRange(
                AnimationHelperView.TranslationPosition.SCREEN_TOP,
                AnimationHelperView.TranslationPosition.CURRENT_POSITION);

        AnimationHelperView
            aQueueCallsToAction = new AnimationHelperView(mQueueCallsToAction);
            aQueueCallsToAction.setTranslationYRange(
                AnimationHelperView.TranslationPosition.SCREEN_BOTTOM,
                AnimationHelperView.TranslationPosition.CURRENT_POSITION);

        AnimationHelperView
            aQueuePositionInfo = new AnimationHelperView(mQueuePositionInfo);
            aQueuePositionInfo.setTranslationYRange(
                AnimationHelperView.TranslationPosition.SCREEN_LEFT,
                AnimationHelperView.TranslationPosition.CURRENT_POSITION);

        AnimationHelperView
            aFootballGuy = new AnimationHelperView(mQueueFootballPlayer);
            aFootballGuy.setTranslationYRange(
                    AnimationHelperView.TranslationPosition.SCREEN_LEFT,
                    AnimationHelperView.TranslationPosition.CURRENT_POSITION);

        // Text and call to action.
        animTextCallToActions = new AnimationHelper(this, 25, 5);
        animTextCallToActions.addHelperView(aQueuePromoText);
        animTextCallToActions.addHelperView(aQueueCallsToAction);

        // Queue container.
        animQueueContainer = new AnimationHelper(this, 25, 10);
        animQueueContainer.addHelperView(aQueuePositionInfo);

        // Football guy.
        animFootballPlayer = new AnimationHelper(this, 100, 30);
        animFootballPlayer.addHelperView(aFootballGuy);
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