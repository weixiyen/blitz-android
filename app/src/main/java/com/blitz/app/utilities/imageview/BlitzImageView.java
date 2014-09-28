package com.blitz.app.utilities.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.blitz.app.R;
import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppDataObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.util.HashMap;

/**
 * Created by mrkcsc on 9/18/14. Copyright 2014 Blitz Studios
 */
public class BlitzImageView extends ImageView {

    // region Member Variables
    // =============================================================================================

    // Should cache the image url.
    private boolean mCacheImageUrl;

    // endregion

    // region Constructors
    // =============================================================================================

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
    // =============================================================================================

    /**
     * Add another way to set an image - specifically
     * a URL from the inter-webs.  Powered
     * by Picasso by square.
     *
     * @param url Target URL, do not include base path.
     */
    @SuppressWarnings("unused")
    public void setImageUrl(String url) {

        setImageUrl(url, null);
    }

    /**
     * Add another way to set an image - specifically
     * a URL from the inter-webs.  Powered
     * by Picasso by square.
     *
     * @param url Target URL, do not include base path.
     * @param maskResourceId Resource image mask.
     */
    @SuppressWarnings("unused")
    public void setImageUrl(String url, final Integer maskResourceId) {

        if (url != null) {

            // Initialize with url prefixed with blitz CDN.
            RequestCreator requestCreator = Picasso.with(getContext())
                    .load(AppConfig.getCDNUrl() + url);

            if (maskResourceId != null) {

                requestCreator = requestCreator
                        .transform(getMaskedTransformation(maskResourceId));
            }

            // Load into image.
            requestCreator.into(this);

            // Update cached url.
            setCachedImageUrl(url);
        }
    }

    // endregion

    // region Private Methods
    // =============================================================================================

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

            // Set with cached url if present.
            setImageUrl(getCachedImageUrl());
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

    /**
     * Fetch a transformer to mask an image bitmap
     * source with some arbitrary image mask.
     *
     * @param maskResourceId Image mask resource.
     *
     * @return Transformation.
     */
    private Transformation getMaskedTransformation(final int maskResourceId) {

        return new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {

                // Fetch mask bitmap.
                Bitmap mask = BitmapFactory.decodeResource
                        (getResources(), maskResourceId);

                // Create a result bitmap of same size as mask.
                Bitmap result = Bitmap.createBitmap(mask.getWidth(),
                        mask.getHeight(), Bitmap.Config.ARGB_8888);


                Canvas mCanvas = new Canvas(result);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                mCanvas.drawBitmap(source, 0, 0, null);
                mCanvas.drawBitmap(mask, 0, 0, paint);
                paint.setXfermode(null);

                // Cleanup.
                source.recycle();

                return result;
            }

            @Override
            public String key() {

                return "blitz()";
            }
        };
    }

    // endregion
}