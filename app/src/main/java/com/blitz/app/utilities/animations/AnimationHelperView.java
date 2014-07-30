package com.blitz.app.utilities.animations;

import android.view.View;

import com.blitz.app.utilities.logging.LogHelper;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringUtil;

/**
 * Created by mrkcsc on 7/30/14.
 */
public class AnimationHelperView {

    private static final int OFF_SCREEN_PADDING = 60;

    private View mView;

    private int mTranslationYFrom;
    private int mTranslationYTo;

    private Integer mHeight;
    private Integer mTop;

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
        switch (from) {
            case SCREEN_TOP:
                mTranslationYFrom = -(mTop + mHeight + OFF_SCREEN_PADDING);
        }

        switch (to) {
            case CURRENT_POSITION:
                mTranslationYTo = 0;
        }

        // Initialize the value.
        translateY(mTranslationYFrom);
    }

    public void translateY(Spring spring) {

        float yTranslation = (float) SpringUtil.mapValueFromRangeToRange
                (spring.getCurrentValue(), 0, 1, mTranslationYFrom, mTranslationYTo);

        translateY(yTranslation);
    }

    public void translateY(float yTranslation) {
        mView.setTranslationY(yTranslation);
    }

    public void setCoordinates() {
        if (mTop == null) {

            int[] location = new int[2];

            mView.getLocationInWindow(location);

            mTop = location[1];
        }

        if (mHeight == null) {
            mHeight = mView.getHeight();
        }

        LogHelper.log("Coordinate: " + mTop + " " + mHeight);
    }
}
