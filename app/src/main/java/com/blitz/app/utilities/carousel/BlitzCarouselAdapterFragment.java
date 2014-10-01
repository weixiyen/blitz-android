package com.blitz.app.utilities.carousel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blitz.app.R;
import com.blitz.app.utilities.image.BlitzImageView;

/**
 * Created by mrkcsc on 8/17/14. Copyright 2014 Blitz Studios
 */
public class BlitzCarouselAdapterFragment extends Fragment {

    // region Member Variables
    // =============================================================================================

    // Bundle parameters
    public static final String PARAM_CAROUSEL_ITEM_ID  = "PARAM_CAROUSEL_ITEM_ID";
    public static final String PARAM_CAROUSEL_ITEM_URL = "PARAM_CAROUSEL_ITEM_URL";
    public static final String PARAM_CAROUSEL_SCALE    = "PARAM_CAROUSEL_SCALE";

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Create a new instance of the fragment.
     *
     * @param context Context.
     * @param scale Scale of the view.
     * @param carouselItemId Item id.
     * @param carouselItemUrl Item url.
     *
     * @return Instance of the fragment.
     */
    public static Fragment newInstance(Context context, float scale,
                                       String carouselItemId, String carouselItemUrl) {

        Bundle bundle = new Bundle();

        bundle.putString(PARAM_CAROUSEL_ITEM_ID, carouselItemId);
        bundle.putString(PARAM_CAROUSEL_ITEM_URL, carouselItemUrl);

        bundle.putFloat(PARAM_CAROUSEL_SCALE, scale);

        return Fragment.instantiate(context,
                BlitzCarouselAdapterFragment.class.getName(), bundle);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * When view is created, inflate associated view
     * and configure it with associated parameters.
     *
     * @param inflater Inflater.
     * @param container Container.
     * @param savedInstanceState Instance state.
     *
     * @return Inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        // Inflate the target view.
        BlitzCarouselScalingView helmetView = (BlitzCarouselScalingView)inflater
                .inflate(R.layout.blitz_carousel_helmet, container, false);

        BlitzImageView helmetImage = (BlitzImageView)helmetView
                .findViewById(R.id.blitz_carousel_helmet_image);

        if (getArguments() != null) {

            String itemUrl = getArguments().getString(PARAM_CAROUSEL_ITEM_URL);
            String itemId = getArguments().getString(PARAM_CAROUSEL_ITEM_ID);

            // Get current scale size.
            float itemScale = getArguments().getFloat(PARAM_CAROUSEL_SCALE);

            helmetView.setTag(itemId);
            helmetView.setScale(itemScale);

            helmetImage.setImageUrl(itemUrl);
        }

        return helmetView;
    }

    // endregion
}