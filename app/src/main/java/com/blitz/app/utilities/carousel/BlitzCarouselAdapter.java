package com.blitz.app.utilities.carousel;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.blitz.app.R;
import com.blitz.app.utilities.reflection.ReflectionHelper;
import com.blitz.app.utilities.viewpager.BlitzViewPagerAdapter;

import java.util.List;

/**
 * Created by mrkcsc on 8/17/14. Copyright 2014 Blitz Studios
 */
public class BlitzCarouselAdapter extends BlitzViewPagerAdapter implements
        ViewPager.OnPageChangeListener {

    // region Member Variables
    // ============================================================================================================

    // Makes view pager infinite for
    // all practical purposes.
    final static int LOOPS = 1000;

    // Min and max scaling.
    final static float MAX_SCALE = 1.0f;
    final static float MIN_SCALE = 0.7f;

    // Difference between mix and max scales.
    final static float DIFF_SCALE = MAX_SCALE - MIN_SCALE;

    private Context mContext;

    private int mPageCount;
    private int mFirstPage;

    private List<String> mAvatarIds;
    private List<String> mAvatarUrls;

    // Handle to the adapter callbacks.
    private Callbacks mCallbacks;

    // endregion

    // region Constructors
    // ============================================================================================================

    /**
     * Create a new carousel adapter.
     *
     * @param fragmentManager Fragment manager.
     */
    private BlitzCarouselAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Create adapter and configure it carousel style.
     *
     * @param viewPager View pager to be the carousel.
     * @param fragmentManager Fragment manager.
     * @param itemIds List of ids.
     * @param itemUrls List of item urls.
     * @param selectedItemId Selected id.
     * @param callbacks Callbacks.
     */
    public static void createWithViewPager(ViewPager viewPager, FragmentManager fragmentManager,
                                           List<String> itemIds,
                                           List<String> itemUrls,
                                           String selectedItemId,
                                           Callbacks callbacks) {

        // Assert valid id's and urls are provided.
        if (itemIds == null || itemUrls == null || viewPager == null
                || itemIds.size() != itemUrls.size()) {

            return;
        }

        BlitzCarouselAdapter adapter = new BlitzCarouselAdapter
                (fragmentManager);

        // Assign member variables.
        adapter.mContext = viewPager.getContext();

        // Set the callbacks.
        adapter.mCallbacks = callbacks;

        // Set the data source.
        adapter.mAvatarIds = itemIds;
        adapter.mAvatarUrls = itemUrls;

        // Page count is the id count.
        adapter.mPageCount = itemIds.size();
        adapter.mFirstPage = itemIds.size() * LOOPS / 2
                + itemIds.indexOf(selectedItemId);

        // Assign the adapter.
        viewPager.setAdapter(adapter);

        // Assign it as page change listener.
        viewPager.setOnPageChangeListener(adapter);

        // Set current item to the middle page so we can
        // fling to both directions left and right.
        viewPager.setCurrentItem(adapter.mFirstPage);

        // Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see.
        viewPager.setOffscreenPageLimit(3);

        // Set padding for pages as a negative number,
        // so a part of next and previous pages will be showed
        int pixelPadding = ReflectionHelper.densityPixelsToPixels
                (viewPager.getContext(), 90);

        // Assign and clip the padding.
        viewPager.setPadding(pixelPadding, 0, pixelPadding, 0);
        viewPager.setClipToPadding(false);
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Fetch item at specified position.
     *
     * @param position Current position.
     *
     * @return Fragment at position.
     */
    @Override
    public Fragment getItem(int position) {

        // Make the first pager bigger than others.
        float mScale = position == mFirstPage ? MAX_SCALE : MIN_SCALE;

        position = position % mPageCount;

        return BlitzCarouselAdapterFragment.newInstance(mContext, mScale,
                mAvatarIds.get(position), mAvatarUrls.get(position));
    }

    /**
     * Fetch count, will be a multiple of the
     * actual page count to allow for
     * infinite scrolling.
     *
     * @return Page count.
     */
    @Override
    public int getCount() {

        return mPageCount * LOOPS;
    }

    /**
     * Set the scale values when page changes.
     *
     * @param position Current position.
     * @param positionOffset Position offset.
     * @param positionOffsetPixels Offset in pixels.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {

        if (positionOffset >= 0f && positionOffset <= 1f) {

            BlitzCarouselScalingView rootViewCurrent = (BlitzCarouselScalingView)
                    getRegisteredRootView(position, R.id.blitz_carousel_helmet);
            BlitzCarouselScalingView rootViewNext = (BlitzCarouselScalingView)
                    getRegisteredRootView(position + 1, R.id.blitz_carousel_helmet);

            rootViewCurrent.setScale
                    (MAX_SCALE - DIFF_SCALE * positionOffset);
            rootViewNext.setScale
                    (MIN_SCALE + DIFF_SCALE * positionOffset);
        }
    }

    /**
     * No need to do anything here.
     *
     * @param position New page position.
     */
    @Override
    public void onPageSelected(int position) {

        // Get the true position.
        position = position % mPageCount;

        if (mCallbacks != null) {
            mCallbacks.onCarouselItemSelected(mAvatarIds.get(position));
        }
    }

    /**
     * No need to do anything here.
     *
     * @param state New state.
     */
    @Override
    public void onPageScrollStateChanged(int state) { }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks {

        public void onCarouselItemSelected(String itemId);
    }

    // endregion
}