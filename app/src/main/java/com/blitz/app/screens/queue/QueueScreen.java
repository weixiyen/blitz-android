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

    @InjectView(R.id.queue_promo_text) View mQueuePromoText;

    private AnimationHelper mPageAnimations;

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

        // Initialize helper animations.
        mPageAnimations = new AnimationHelper(this);

        // Add helper animation views.
        mPageAnimations.addHelperView(aQueuePromoText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Enable.
        mPageAnimations.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Disable.
        mPageAnimations.disable();
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