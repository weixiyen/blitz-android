package com.blitz.app.utilities.animations;

import android.view.View;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringUtil;

/**
 * Created by mrkcsc on 7/30/14.
 */
public class AnimHelperView {

    private static final int OFF_SCREEN_PADDING = 60;

    private View mView;

    private boolean mTranslateY;
    private int mTranslationYFrom;
    private int mTranslationYTo;

    private boolean mTranslateX;
    private int mTranslationXFrom;
    private int mTranslationXTo;

    private AnimHelperPresets mPreset;

    private Integer mViewWidth;
    private Integer mViewHeight;
    private Integer mViewTop;
    private Integer mViewLeft;

    private int mWindowHeight;
    private int mWindowWidth;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Unused private constructor.
     */
    @SuppressWarnings("unused")
    private AnimHelperView() {

    }

    /**
     * Internal constructor for initializing
     * a new instance of the helper.
     *
     * @param view The view to operate on.
     */
    private AnimHelperView(View view) {
        mView = view;
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Static initializer of a new animation
     * helper view.
     *
     * @param view View being operated on.
     *
     * @return New instance of the helper.
     */
    @SuppressWarnings("unused")
    public static AnimHelperView from(View view) {

        return new AnimHelperView(view);
    }

    /**
     * Static initializer of a new animation
     * helper view.
     *
     * @param view View being operated on.
     * @param presets Preset enum.
     *
     * @return New instance of the helper.
     */
    @SuppressWarnings("unused")
    public static AnimHelperView from(View view, AnimHelperPresets presets) {

        return new AnimHelperView(view).withPreset(presets);
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Sets an animation preset for this helper.
     *
     * @param present Preset enum.
     *
     * @return Current class instance.
     */
    private AnimHelperView withPreset(AnimHelperPresets present) {

        mPreset = present;

        return this;
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

        switch (mPreset) {
            case SLIDE_DOWN:
                mTranslationYFrom = -(mViewTop + mViewHeight + OFF_SCREEN_PADDING);
                mTranslationYTo = 0;
                mTranslateY = true;
                break;
            case SLIDE_UP:
                mTranslationYFrom =  mWindowHeight - mViewTop;
                mTranslationYTo = 0;
                mTranslateY = true;
                break;
            case SLIDE_RIGHT:
                mTranslationXFrom = -(mViewLeft + mViewWidth + OFF_SCREEN_PADDING);
                mTranslationXTo = 0;
                mTranslateX = true;
            case SLIDE_LEFT:
                mTranslationXFrom = mWindowWidth - mViewLeft;
                mTranslationXTo = 0;
                mTranslateX = true;
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