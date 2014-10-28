package com.blitz.app.utilities.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.blitz.app.R;
import com.blitz.app.utilities.app.AppDataObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrkcsc on 9/18/14. Copyright 2014 Blitz Studios
 */
public class BlitzImageView extends ImageView {

    // region Member Variables
    // ============================================================================================================

    // Should cache the image url.
    private boolean mCacheImageUrl;

    // Image url, if any.
    private String mImageUrl;

    // endregion

    // region Constructors
    // ============================================================================================================

    @SuppressWarnings("unused")
    public BlitzImageView(Context context) {
        super(context);
    }

    @SuppressWarnings("unused")
    public BlitzImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Fetch custom attributes.
        TypedArray styledAttributes = getContext().obtainStyledAttributes
                (attrs, R.styleable.BlitzImageView);

        setupClearDefault(styledAttributes);

        setupCachedImageURL(styledAttributes);

        // Done reading attributes.
        styledAttributes.recycle();
    }

    @SuppressWarnings("unused")
    public BlitzImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Fetch custom attributes.
        TypedArray styledAttributes = getContext().obtainStyledAttributes
                (attrs, R.styleable.BlitzImageView, defStyle, 0);

        setupClearDefault(styledAttributes);

        setupCachedImageURL(styledAttributes);

        // Done reading attributes.
        styledAttributes.recycle();
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Add another way to set an image - specifically
     * a URL from the inter-webs.  Powered
     * by Picasso by square.
     *
     * @param imageUrl Target URL, do not include base path.
     */
    @SuppressWarnings("unused")
    public void setImageUrl(String imageUrl) {

        setImageUrl(imageUrl, null);
    }

    /**
     * Add another way to set an image - specifically
     * a URL from the inter-webs.  Powered
     * by Picasso by square.
     *
     * @param imageUrl Target URL, do not include base path.
     * @param maskAssetUrl Target asset url, should live in assets directory.
     */
    @SuppressWarnings("unused")
    public void setImageUrl(String imageUrl, final String maskAssetUrl) {

        setImageUrl(imageUrl, maskAssetUrl, false);
    }

    /**
     * Add another way to set an image - specifically
     * a URL from the inter-webs.  Powered
     * by Picasso by square.
     *
     * @param imageUrl Target URL, do not include base path.
     * @param maskAssetUrl Target asset url, should live in assets directory.
     * @param cacheUrlOnly If set, we only want to cache the url and not download
     *                     and set the image.
     */
    @SuppressWarnings("unused")
    public void setImageUrl(String imageUrl, final String maskAssetUrl, boolean cacheUrlOnly) {

        // Set the url.
        mImageUrl = imageUrl;

        if (!cacheUrlOnly) {

            // Load image url and mask url.
            BlitzImage.from(getContext()).loadImageUrl(mImageUrl, maskAssetUrl,
                    new BlitzImage.CallbackImageUrls() {

                @Override
                public void onSuccess(Map<String, Bitmap> images) {

                    if (images.containsKey(mImageUrl)) {

                        setImageBitmap(images.get(mImageUrl));
                    }
                }
            });
        }

        // Update cached url.
        setCachedImageUrl(mImageUrl);
    }

    /**
     * Fetch currently set image url.
     *
     * @return Image url (if any).
     */
    @SuppressWarnings("unused")
    public String getImageUrl() {

        return mImageUrl;
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Fetch a cached image url.
     *
     * @return Cached url, or null if it does not exist.
     */
    private String getCachedImageUrl() {

        // Can only cache if resource id is set.
        if (mCacheImageUrl && getId() != -1) {

            HashMap<String, String> cachedImageUrls = AppDataObject.cachedImageUrls.get();

            if (cachedImageUrls.containsKey(getResources().getResourceName(getId()))) {

                return cachedImageUrls.get(getResources().getResourceName(getId()));
            }
        }

        return null;
    }

    /**
     * Set a cached image url.
     *
     * @param url Target URL, do not include base path.
     */
    private void setCachedImageUrl(String url) {

        if (mCacheImageUrl && getId() != -1) {

            HashMap<String, String> cachedImageUrls = AppDataObject.cachedImageUrls.get();

            // Update dictionary with new url.
            cachedImageUrls.put(getResources().getResourceName(getId()), url);

            AppDataObject.cachedImageUrls.set(cachedImageUrls);
        }
    }

    /**
     * Setup image url caching.
     *
     * @param styledAttributes Custom attributes.
     */
    private void setupCachedImageURL(TypedArray styledAttributes) {

        if (!isInEditMode() && styledAttributes.hasValue(R.styleable.BlitzImageView_cacheImageURL)) {

            mCacheImageUrl = styledAttributes.getBoolean
                    (R.styleable.BlitzImageView_cacheImageURL, false);

            if (mCacheImageUrl) {

                // Set with cached url if present.
                setImageUrl(getCachedImageUrl());
            }
        }
    }

    /**
     * If set, clear out any image set on this image view.
     *
     * Allows the developer to have good previews of
     * the XML but not have it have any sort of flicker
     * when loading the real text from the network.
     *
     * @param styledAttributes Custom attributes.
     */
    private void setupClearDefault(TypedArray styledAttributes) {


        if (!isInEditMode() && styledAttributes.hasValue(R.styleable.BlitzImageView_clearInitialContent)) {

            boolean clearDefault = styledAttributes.getBoolean
                    (R.styleable.BlitzImageView_clearInitialContent, false);

            if (clearDefault) {

                setImageDrawable(null);
            }
        }
    }

    // endregion
}