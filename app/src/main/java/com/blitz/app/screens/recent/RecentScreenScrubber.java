package com.blitz.app.screens.recent;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.blitz.app.utilities.logging.LogHelper;

/**
 * Created by mrkcsc on 10/14/14. Copyright 2014 Blitz Studios
 */
public class RecentScreenScrubber extends LinearLayout {

    private ViewPager mViewPager;

    public RecentScreenScrubber(Context context) {
        super(context);

        setupTouchEvents();
    }

    public RecentScreenScrubber(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupTouchEvents();
    }

    public RecentScreenScrubber(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setupTouchEvents();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // do whatever you want with the event
        // and return true so that children don't receive it
        //LogHelper.log("TOuched 2");

        return true;
    }

    public void setViewPager(ViewPager viewPager) {

        mViewPager = viewPager;
    }

    private void setupTouchEvents() {

        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_MOVE:

                            // Process the scrub event.
                            processScrubEvent(view.getLeft(), motionEvent.getX());

                            // Disable view pager.
                            if (mViewPager != null) {
                                mViewPager.requestDisallowInterceptTouchEvent(true);
                            }

                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:

                            // Restore view pager.
                            if (mViewPager != null) {
                                mViewPager.requestDisallowInterceptTouchEvent(false);
                            }

                            break;
                    }

                return true;
            }
        });
    }

    private void processScrubEvent(float xPosScrubberStart, float xPosCurrent) {

        LogHelper.log("Scrubbed: " + xPosScrubberStart + " position current: " + xPosCurrent);
    }
}