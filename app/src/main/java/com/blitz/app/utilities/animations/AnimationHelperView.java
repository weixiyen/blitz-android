package com.blitz.app.utilities.animations;

import android.view.View;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringUtil;

/**
 * Created by mrkcsc on 7/30/14.
 */
public class AnimationHelperView {

    private static final int OFF_SCREEN_PADDING = 60;

    private View mView;

    private boolean mTranslateY;
    private int mTranslationYFrom;
    private int mTranslationYTo;

    private boolean mTranslateX;
    private int mTranslationXFrom;
    private int mTranslationXTo;

    private TranslationPosition mFrom;
    private TranslationPosition mTo;

    private Integer mViewWidth;
    private Integer mViewHeight;
    private Integer mViewTop;
    private Integer mViewLeft;

    private int mWindowHeight;
    private int mWindowWidth;

    public enum TranslationPosition {
        CURRENT_POSITION,

        SCREEN_TOP,
        SCREEN_BOTTOM,
        SCREEN_LEFT,
        SCREEN_RIGHT
    }

    @SuppressWarnings("unused")
    private AnimationHelperView() {

    }

    public AnimationHelperView(View view) {
        mView = view;
    }

    public void setTranslationYRange(TranslationPosition from, TranslationPosition to) {
        mFrom = from;
        mTo   = to;
    }

    void setCoordinates(int windowWidth, int windowHeight) {

        // Set window dimensions.
        mWindowWidth = windowWidth;
        mWindowHeight = windowHeight;

        if (mViewTop == null || mViewLeft == null) {

            int[] location = new int[2];

            mView.getLocationInWindow(location);

            mViewLeft = location[0];
            mViewTop = location[1];
        }

        if (mViewHeight == null || mViewWidth == null) {

            // Set height and width.
            mViewHeight = mView.getHeight();
            mViewWidth  = mView.getWidth();

            tryInitialize();
        }
    }

    private void tryInitialize() {

        switch (mFrom) {
            case SCREEN_TOP:
                mTranslationYFrom = -(mViewTop + mViewHeight + OFF_SCREEN_PADDING);
                mTranslateY = true;
                break;
            case SCREEN_BOTTOM:
                mTranslationYFrom =  mWindowHeight - mViewTop;
                mTranslateY = true;
                break;
            case SCREEN_LEFT:
                mTranslationXFrom = -(mViewLeft + mViewWidth + OFF_SCREEN_PADDING);
                mTranslateX = true;
        }

        switch (mTo) {
            case CURRENT_POSITION:
                mTranslationYTo = 0;
                mTranslationXTo = 0;
                break;
        }

        // Initialize the value.
        animateWithSpring(null);
    }

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

    /**
     * Apply animations - either pre-defined
     * or custom, given an animation spring.
     *
     * @param spring Animation spring, initializes if null.
     */
    void animateWithSpring(Spring spring) {

        if (mTranslateY) {

            float yTranslation = mTranslationYFrom;

            if (spring != null) {

                // Standard y-translation mapping.
                yTranslation = (float) SpringUtil.mapValueFromRangeToRange
                        (spring.getCurrentValue(), 0, 1, mTranslationYFrom, mTranslationYTo);
            }

            // Apply the translation.
            mView.setTranslationY(yTranslation);
        }

        if (mTranslateX) {

            float xTranslation = mTranslationXFrom;

            if (spring != null) {

                // Standard x-translation mapping.
                xTranslation = (float) SpringUtil.mapValueFromRangeToRange
                        (spring.getCurrentValue(), 0, 1, mTranslationXFrom, mTranslationXTo);
            }

            // Apply the translation.
            mView.setTranslationX(xTranslation);
        }
    }
}