package com.blitz.app.screens.queue;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.blitz.app.R;
import com.blitz.app.screens.access_code.AccessCodeScreen;
import com.blitz.app.screens.sign_in.SignInScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.animations.AnimationHelper;
import com.blitz.app.utilities.animations.AnimationHelperView;
import com.blitz.app.utilities.logging.LogHelper;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;

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

    private Spring mSpring;

    private final OpeningAnimations mSpringListener = new OpeningAnimations();

    private Integer mYTranslationOffset;

    private AnimationHelperView aQueuePromoText;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        aQueuePromoText = new AnimationHelperView(mQueuePromoText);

        // Initialize spring.
        mSpring = AnimationHelper.createSpring(40, 1);

        final View rootView = ((FrameLayout)findViewById(android.R.id.content)).getChildAt(0);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        aQueuePromoText.setCoordinates();
                        aQueuePromoText.setTranslationYRange(
                                AnimationHelperView.TranslationPosition.SCREEN_TOP,
                                AnimationHelperView.TranslationPosition.CURRENT_POSITION);

                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        LogHelper.log("Ready, waiting to play animations.");

        mSpring.addListener(mSpringListener);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSpring.setEndValue(1);

                LogHelper.log("Now playing");
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSpring.removeAllListeners();
        mSpring.setEndValue(0);
    }

    private class OpeningAnimations extends SimpleSpringListener {

        @Override
        public void onSpringUpdate(Spring spring) {

            aQueuePromoText.translateY(spring);
        }
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