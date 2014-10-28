package com.blitz.app.utilities.carousel;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by mrkcsc on 8/17/14. Copyright 2014 Blitz Studios
 *
 * This is a view group that respects a scaling
 * property as it draws, it is used to create
 * a scaling animation as user moves the carousel.
 */
public class BlitzCarouselScalingView extends LinearLayout {

    // region Member Variables
    // ============================================================================================================

    // Initialize the scale to maximum scale.
    private float mScale = BlitzCarouselAdapter.MAX_SCALE;

    // endregion

    // region Constructors
    // ============================================================================================================

    @SuppressWarnings("unused")
    public BlitzCarouselScalingView(Context context) {
        super(context);
    }

    @SuppressWarnings("unused")
    public BlitzCarouselScalingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unused")
    public BlitzCarouselScalingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // endregion

    // region Protected Methods
    // ============================================================================================================

    /**
     * Scales the canvas before drawing it.
     *
     * @param canvas Da canvas.
     */
    @Override
    protected void onDraw(Canvas canvas) {

        int width  = getWidth();
        int height = getHeight();

        // Set the scale.
        canvas.scale(mScale, mScale, width / 2, height / 2);

        super.onDraw(canvas);
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Set the scale.
     *
     * @param scale The scale.
     */
    public void setScale(float scale) {

        mScale = scale;

        // This calls on draw to
        // update with new scale.
        invalidate();
    }

    // endregion
}