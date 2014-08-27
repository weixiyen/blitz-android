package com.blitz.app.utilities.animations;

import android.view.View;

import com.blitz.app.R;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringUtil;

/**
 * Created by mrkcsc on 7/30/14. Copyright 2014 Blitz Studios
 */
public class AnimHelperSpringsView {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    private static final int OFF_SCREEN_PADDING = 60;

    // Target view.
    private View mView;

    // Translate Y information.
    private boolean mTranslateY;
    private int     mTranslationYFrom;
    private int     mTranslationYTo;

    // Translate X information.
    private boolean mTranslateX;
    private int     mTranslationXFrom;
    private int     mTranslationXTo;

    // Zoom information.
    private boolean mScale;
    private float   mScaleFrom;
    private float   mScaleTo;

    // Window dimensions.
    private int mWindowHeight;
    private int mWindowWidth;

    // Animation path preset.
    private AnimHelperSpringsPresets mPreset;

    // View layout information.
    private ViewLayout mViewLayout;

    /**
     * Metadata about the view layout.
     */
    private class ViewLayout {

        public Integer mViewWidth;
        public Integer mViewHeight;
        public Integer mViewTop;
        public Integer mViewLeft;
    }

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Unused private constructor.
     */
    @SuppressWarnings("unused")
    private AnimHelperSpringsView() {

    }

    /**
     * Internal constructor for initializing
     * a new instance of the helper.
     *
     * @param view The view to operate on.
     */
    private AnimHelperSpringsView(View view) {
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
    public static AnimHelperSpringsView from(View view) {

        return new AnimHelperSpringsView(view);
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
    public static AnimHelperSpringsView from(View view, AnimHelperSpringsPresets presets) {

        return new AnimHelperSpringsView(view).withPreset(presets);
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
    private AnimHelperSpringsView withPreset(AnimHelperSpringsPresets present) {

        mPreset = present;

        return this;
    }

    private void tryInitialize() {

        switch (mPreset) {
            case SLIDE_DOWN:
                mTranslationYFrom = -(mViewLayout.mViewTop + mViewLayout.mViewHeight + OFF_SCREEN_PADDING);
                mTranslationYTo   = 0;
                mTranslateY = true;
                break;
            case SLIDE_DOWN_REVERSED:
                mTranslationYFrom = 0;
                mTranslationYTo   = -(mViewLayout.mViewTop + mViewLayout.mViewHeight + OFF_SCREEN_PADDING);
                mTranslateY = true;
                break;
            case SLIDE_UP:
                mTranslationYFrom = mWindowHeight - mViewLayout.mViewTop;
                mTranslationYTo   = 0;
                mTranslateY = true;
                break;
            case SLIDE_UP_REVERSED:
                mTranslationYFrom = 0;
                mTranslationYTo   = mWindowHeight - mViewLayout.mViewTop;
                mTranslateY = true;
                break;
            case SLIDE_RIGHT:
                mTranslationXFrom = -(mViewLayout.mViewLeft + mViewLayout.mViewWidth + OFF_SCREEN_PADDING);
                mTranslationXTo   = 0;
                mTranslateX = true;
                break;
            case SLIDE_RIGHT_REVERSED:
                mTranslationXFrom = 0;
                mTranslationXTo   = -(mViewLayout.mViewLeft + mViewLayout.mViewWidth + OFF_SCREEN_PADDING);
                mTranslateX = true;
                break;
            case SLIDE_LEFT:
                mTranslationXFrom = mWindowWidth - mViewLayout.mViewLeft;
                mTranslationXTo   = 0;
                mTranslateX = true;
                break;
            case SLIDE_LEFT_REVERSED:
                mTranslationXFrom = 0;
                mTranslationXTo   = mWindowWidth - mViewLayout.mViewLeft;
                mTranslateX = true;
                break;
            case SCALE_UP:
                mScaleFrom = 0;
                mScaleTo   = 1;
                mScale = true;
                break;
        }

        // Initialize the value.
        animateWithSpring(null);
    }

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

    /**
     * Setup the various coordinates of the view
     * associated with this helper.
     *
     * @param windowWidth The window width.
     * @param windowHeight The window height.
     */
    void setCoordinates(int windowWidth, int windowHeight) {

        // Set window dimensions.
        mWindowWidth  = windowWidth;
        mWindowHeight = windowHeight;

        if (mViewLayout == null) {
            mViewLayout = (ViewLayout)mView.getTag(R.string.anim_original_view_layout);
        }

        if (mViewLayout == null) {
            mViewLayout = new ViewLayout();

            // If the view top and left is not set.
            if (mViewLayout.mViewTop == null || mViewLayout.mViewLeft == null) {

                int[] location = new int[2];

                mView.getLocationInWindow(location);

                mViewLayout.mViewLeft = location[0];
                mViewLayout.mViewTop = location[1];
            }

            // If the view height and width is not set.
            if (mViewLayout.mViewHeight == null || mViewLayout.mViewWidth == null) {

                // Set height and width.
                mViewLayout.mViewHeight = mView.getHeight();
                mViewLayout.mViewWidth  = mView.getWidth();
            }

            // Save the layout information.
            mView.setTag(R.string.anim_original_view_layout, mViewLayout);
        }

        tryInitialize();
    }

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

        if (mScale) {

            float scale = mScaleFrom;

            if (spring != null) {

                // Standard scale mapping.
                scale = (float )SpringUtil.mapValueFromRangeToRange
                        (spring.getCurrentValue(), 0, 1, mScaleFrom, mScaleTo);
            }

            mView.setScaleX(scale);
            mView.setScaleY(scale);
        }
    }
}