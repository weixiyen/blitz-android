package com.blitz.app.utilities.viewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Miguel Gaeta on 6/1/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("unused")
public class ViewPagerTransformerZoom implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.50f;

    public void transformPage(View view, float position) {

        // Only fade this screen.
        adjustAlpha(view, position);

        // All pages scale.
        adjustScale(view, position);
    }

    public void adjustScale(View view, float position) {

        if (position >= -1 && position <= 1) {

            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));

            float   verticalMargin = pageHeight * (1 - scaleFactor) / 2;
            float horizontalMargin = pageWidth  * (1 - scaleFactor) / 2;

            if (position < 0) {
                view.setTranslationX( horizontalMargin - verticalMargin / 2);
            } else {
                view.setTranslationX(-horizontalMargin + verticalMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
    }

    public void adjustAlpha(View view, float position) {

        if (position < -1) {

            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) {

            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                              (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else {

            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}