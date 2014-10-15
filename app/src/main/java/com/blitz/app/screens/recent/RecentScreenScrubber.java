package com.blitz.app.screens.recent;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.logging.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrkcsc on 10/14/14. Copyright 2014 Blitz Studios
 */
public class RecentScreenScrubber extends LinearLayout {

    private ViewPager mViewPager;
    private int mScrubberSize;
    private Integer mScrubberItemSelected;
    private List<TextView> mScrubberItems;

    // region Constructors
    // =============================================================================================

    @SuppressWarnings("unused")
    public RecentScreenScrubber(Context context) {
        super(context);

        setupTouchEvents();
    }

    @SuppressWarnings("unused")
    public RecentScreenScrubber(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupTouchEvents();
    }

    @SuppressWarnings("unused")
    public RecentScreenScrubber(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setupTouchEvents();
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Tell the scrubber that we want to intercept
     * all touch events and not give it to the children.
     *
     * @param ev Motion event.
     *
     * @return Always true to eat the event.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return true;
    }

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Provide a view pager if this scrubber
     * lives inside one.  Horizontal scrolling will
     * be disabled when touching the scrubber.
     *
     * @param viewPager Target view pager.
     */
    public void setViewPager(ViewPager viewPager) {

        mViewPager = viewPager;
    }

    /**
     * Set the size of the scrubber.
     *
     * @param scrubberSize Amount of items to scrub.
     */
    public void setSize(int scrubberSize) {

        // Update the size.
        mScrubberSize = scrubberSize;

        // Setup the Items.
        setupScrubberItems();
    }

    /**
     * Set a scrubber item to be selected.
     *
     * @param position Scrubber item index.
     */
    public void setScrubberItemSelected(int position) {

        // if valid position is provided.
        if (position < mScrubberSize && position > 0) {

            // Set selected color.
            mScrubberItems.get(position)
                    .setTextColor(getResources().getColor(R.color.active_blue));

            if (mScrubberItemSelected != null) {

                // Reset color of old selected.
                mScrubberItems.get(mScrubberItemSelected)
                        .setText(getResources().getColor(R.color.text_color_light));
            }

            // Update selected index.
            mScrubberItemSelected = position;
        }
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Inflate scrubber items into the view.
     */
    private void setupScrubberItems() {

        if (mScrubberItems == null) {
            mScrubberItems = new ArrayList<TextView>();
        }

        // Remove older items.
        mScrubberItems.clear();

        for (int i = 0; i < mScrubberSize; i++) {

            // Inflate scrubber item.
            LayoutInflater.from(getContext())
                    .inflate(R.layout.recent_screen_week, this, true);
        }

        // Loop again to get inflated children.
        for (int i = 0; i < mScrubberSize; i++) {

            // Add to array of scrubber item views.
            mScrubberItems.add((TextView)getChildAt(i));
        }
    }

    /**
     * Scrubber intercepts touch events and if inside a view
     * pager, disabled swiping during that time.  Processes
     * the scrub event as horizontal movement.
     */
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

    // endregion
}