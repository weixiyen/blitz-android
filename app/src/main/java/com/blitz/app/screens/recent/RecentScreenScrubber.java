package com.blitz.app.screens.recent;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.animations.AnimHelperFade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrkcsc on 10/14/14. Copyright 2014 Blitz Studios
 */
public class RecentScreenScrubber extends LinearLayout {

    // region Constructors
    // ============================================================================================================

    // Optional parent view pager.
    private ViewPager mViewPager;

    // Total scrubber dots.
    private int mScrubberSize;

    // Selected scrubber index.
    private Integer mScrubberItemSelected;

    // Scrubber views.
    private List<TextView> mScrubberItems;

    // Position information.
    private Integer mScrubberItemWidth;
    private Integer mScrubberItemLeftStart;

    // Scrubber item colors.
    private Integer mScrubberColor;
    private Integer mScrubberColorActive;

    // Text view that shows active scrubber item.
    private TextView mScrubberTextView;
    private boolean mScrubberTextViewVisible;

    // Handle to the callbacks.
    private Callbacks mCallbacks;

    // endregion

    // region Constructors
    // ============================================================================================================

    @SuppressWarnings("unused")
    public RecentScreenScrubber(Context context) {
        super(context);

        setupTouchEvents();
        setupScrubberColors();
    }

    @SuppressWarnings("unused")
    public RecentScreenScrubber(Context context, AttributeSet attrs) {
        super(context, attrs);

        setupTouchEvents();
        setupScrubberColors();
    }

    @SuppressWarnings("unused")
    public RecentScreenScrubber(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setupTouchEvents();
        setupScrubberColors();
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

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
    // ============================================================================================================

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
        if (position < mScrubberSize && position >= 0
                && (mScrubberItemSelected == null || position != mScrubberItemSelected)) {

            // Set selected color.
            mScrubberItems.get(position).setTextColor(mScrubberColorActive);

            if (mScrubberItemSelected != null) {

                // Reset color of old selected.
                mScrubberItems.get(mScrubberItemSelected).setTextColor(mScrubberColor);
            }

            if (mScrubberTextView != null) {
                mScrubberTextView.setText("Week " + (position + 1));
            }

            // Update selected index.
            mScrubberItemSelected = position;
        }
    }

    /**
     * Set text view to show current scrubber item.
     *
     * @param scrubberTextView Text view.
     */
    public void setScrubberTextView(TextView scrubberTextView) {

        mScrubberTextView = scrubberTextView;

        if (mScrubberTextView != null) {
            mScrubberTextView.setVisibility(GONE);
        }
    }

    /**
     * Set the callbacks.
     *
     * @param callbacks Callback object.
     */
    public void setCallbacks(Callbacks callbacks) {

        mCallbacks = callbacks;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Fetch scrubber item colors.
     */
    private void setupScrubberColors() {

        try {

            // Fetch the associated colors for the scrubber.
            mScrubberColor       = getResources().getColor(R.color.text_color_light);
            mScrubberColorActive = getResources().getColor(R.color.active_blue);

        } catch (Resources.NotFoundException ignored) { }
    }

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

                if (motionEvent != null) {

                    try {

                        switch (motionEvent.getAction()) {

                            case MotionEvent.ACTION_MOVE:

                                // Show the text view.
                                setScrubberTextViewVisibility(true);

                                // Process the scrub event.
                                processScrubEvent(motionEvent.getX());

                                // Disable view pager.
                                if (mViewPager != null) {
                                    mViewPager.requestDisallowInterceptTouchEvent(true);
                                }

                                break;
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:

                                // Hide the text view.
                                setScrubberTextViewVisibility(false);

                                // Restore view pager.
                                if (mViewPager != null) {
                                    mViewPager.requestDisallowInterceptTouchEvent(false);
                                }

                                // Emit selection event.
                                if (mCallbacks != null) {
                                    mCallbacks.onScrubberItemSelected(mScrubberItemSelected);
                                }

                                break;
                        }
                    } catch (Exception ignored) { }
                }

                return true;
            }
        });
    }

    /**
     * Toggle the visibility of the text view.  This event
     * may be called multiple time so guard it
     * with a change event flag.
     *
     * @param visible Is visible.
     */
    private void setScrubberTextViewVisibility(boolean visible) {

        if (mScrubberTextView != null) {

            if (mScrubberTextViewVisible != visible) {
                mScrubberTextViewVisible  = visible;

                // Set visibility on change events.
                AnimHelperFade.setVisibility(mScrubberTextView, visible ? VISIBLE : GONE);
            }
        }
    }

    /**
     * Process a scrub event.
     *
     * @param xPosCurrent Current x position.
     */
    private void processScrubEvent(float xPosCurrent) {

        if (mScrubberSize > 0) {

            // Get individual item width.
            if (mScrubberItemWidth == null) {
                mScrubberItemWidth = getChildAt(0).getWidth();
            }

            // Get start of the first item.
            if (mScrubberItemLeftStart == null) {
                mScrubberItemLeftStart = getChildAt(0).getLeft();
            }

            // Use some maths to get the selected item, then normalize it.
            int scrubberItemSelected = (int)((xPosCurrent - mScrubberItemLeftStart) / mScrubberItemWidth);

            if (scrubberItemSelected < 0) {
                scrubberItemSelected = 0;
            }

            if (scrubberItemSelected >= mScrubberSize) {
                scrubberItemSelected  = mScrubberSize - 1;
            }

            // If a new item is selected.
            if (mScrubberItemSelected != null && mScrubberItemSelected != scrubberItemSelected) {

                setScrubberItemSelected(scrubberItemSelected);
            }
        }
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks {

        public void onScrubberItemSelected(int position);
    }

    // endregion
}