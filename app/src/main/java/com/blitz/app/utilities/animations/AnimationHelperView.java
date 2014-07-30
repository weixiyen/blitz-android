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

    private int mTranslationYFrom;
    private int mTranslationYTo;

    private TranslationPosition mFrom;
    private TranslationPosition mTo;

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
        mFrom = from;
        mTo   = to;
    }

    void translateY(Spring spring) {

        float yTranslation = (float) SpringUtil.mapValueFromRangeToRange
                (spring.getCurrentValue(), 0, 1, mTranslationYFrom, mTranslationYTo);

        translateY(yTranslation);
    }

    void translateY(float yTranslation) {
        mView.setTranslationY(yTranslation);
    }

    void setCoordinates() {
        if (mTop == null) {

            int[] location = new int[2];

            mView.getLocationInWindow(location);

            mTop = location[1];
        }

        if (mHeight == null) {
            mHeight = mView.getHeight();

            tryInitialize();
        }
    }

    private void tryInitialize() {
        switch (mFrom) {
            case SCREEN_TOP:
                mTranslationYFrom = -(mTop + mHeight + OFF_SCREEN_PADDING);
                break;
        }

        switch (mTo) {
            case CURRENT_POSITION:
                mTranslationYTo = 0;
                break;
        }

        // Initialize the value.
        translateY(mTranslationYFrom);
    }
}
