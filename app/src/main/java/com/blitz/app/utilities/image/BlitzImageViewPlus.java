package com.blitz.app.utilities.image;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;

import com.blitz.app.R;

/**
 * Copyright 2013 MeetMe, Inc.
 *
 * https://github.com/MeetMe/Android-ImageViewPlus/issues
 */
public class BlitzImageViewPlus extends ImageView {

    private static final int CONTENT_LAYER_ID_EMPTY = -1;

    /**
     * The default drawable, used whenever nothing is set as <code>@src</code>, or when the current Drawable has been recycled.
     */
    private Drawable mDefaultDrawable = null;

    /**
     * The layer drawable to wrap the content
     *
     * @see R.attr#layerDrawable
     */
    private LayerDrawable mLayerDrawable;

    /**
     * The layer id in {@link #mLayerDrawable} to replace content with
     *
     * @see R.attr#contentLayerId
     */
    private int mContentLayerId = CONTENT_LAYER_ID_EMPTY;

    private DefaultDrawableListener mListener = null;

    private PlusScaleType mScaleType;

    private Drawable mContentDrawable;

    /**
     * The tint/overlay color or ColorStateList used to overlay the image.
     */
    private ColorStateList mTintColorList;

    private int mContentResource;

    private Uri mContentUri;

    public BlitzImageViewPlus(final Context context) {
        this(context, null);
    }

    public BlitzImageViewPlus(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlitzImageViewPlus(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Context context, AttributeSet attrs, int defStyle) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BlitzImageViewPlus, 0, 0);

            if (a != null) {
                Drawable defaultDrawable = a.getDrawable(R.styleable.BlitzImageViewPlus_defaultDrawable);
                Drawable selectorDrawable = a.getDrawable(R.styleable.BlitzImageViewPlus_layerDrawable);
                int selectorLayerId = a.getResourceId(R.styleable.BlitzImageViewPlus_contentLayerId, CONTENT_LAYER_ID_EMPTY);
                int scaleType = a.getInt(R.styleable.BlitzImageViewPlus_scaleType, -1);

                mTintColorList = a.getColorStateList(R.styleable.BlitzImageViewPlus_overlayTintColor);

                a.recycle();

                if (scaleType >= 0) {
                    setScaleType(PlusScaleType.getScaleType(scaleType));
                }

                if (defaultDrawable != null) {
                    setDefaultDrawable(defaultDrawable);
                }

                if (selectorDrawable instanceof LayerDrawable) { // We don't check the value of the selectorLayerId as
                    // #setSelectorResources handles that check
                    setLayerResources((LayerDrawable) selectorDrawable, selectorLayerId);
                }

                if (mTintColorList != null) {
                    int color = mTintColorList.getDefaultColor();

                    if (color != Color.TRANSPARENT) {
                        setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
        }
    }

    /**
     * Sets a drawable as the content of this ImageView.
     * <p/>
     * This does Bitmap reading and decoding on the UI thread, which can cause a latency hiccup. If that's a concern, consider using
     * {@link #setImageDrawable(android.graphics.drawable.Drawable)} or {@link #setImageBitmap(android.graphics.Bitmap)} and
     * {@link android.graphics.BitmapFactory} instead.
     *
     * @param resId the resource identifier of the the drawable
     */
    @Override
    public void setImageResource(int resId) {
        if (mContentUri != null || mContentResource != resId) {
            updateDrawable(null);
            mContentResource = resId;
            mContentUri = null;

            resolveUri();
        }
    }

    /**
     * Sets the content of this ImageView to the specified Uri.
     * <p/>
     * <p class="note">
     * This does Bitmap reading and decoding on the UI thread, which can cause a latency hiccup. If that's a concern, consider using
     * {@link #setImageDrawable(android.graphics.drawable.Drawable)} or {@link #setImageBitmap(android.graphics.Bitmap)} and
     * {@link android.graphics.BitmapFactory} instead.
     * </p>
     *
     * @param uri The Uri of an image
     */
    @Override
    public void setImageURI(Uri uri) {
        if (mContentResource != 0 ||
                (mContentUri != uri &&
                        (uri == null || mContentUri == null || !uri.equals(mContentUri)))) {
            updateDrawable(null);
            mContentResource = 0;
            mContentUri = uri;

            resolveUri();
        }
    }

    /**
     * Sets a drawable as the content of this ImageView
     *
     * @param drawable The drawable to set
     */
    @Override
    public void setImageDrawable(Drawable drawable) {
        if (mContentDrawable != drawable) {
            mContentResource = 0;
            mContentUri = null;

            updateDrawable(drawable);
        }
    }

    /**
     * Returns the content drawable of this ImageView
     */
    @Override
    public Drawable getDrawable() {
        return mContentDrawable;
    }

    /**
     * Sets a Bitmap as the content of this ImageView.
     *
     * @param bm The bitmap to set
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        // if this is used frequently, may handle bitmaps explicitly
        // to reduce the intermediate drawable object
        setImageDrawable(new BitmapDrawable(getContext().getResources(), bm));
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mTintColorList != null && !isInEditMode()) {
            Drawable d = getDrawable();

            if (d instanceof LayerDrawable) {
                clearColorFilter();
                // there is a bug with LayerDrawable and an ImageView's ColorFilter, so apply it directly to the LayerDrawable
                applyColorFilterToLayerDrawable((LayerDrawable) d);
            } else if (mTintColorList.isStateful()) {
                int color = mTintColorList.getColorForState(getDrawableState(), Color.TRANSPARENT);

                if (color == Color.TRANSPARENT) {
                    clearColorFilter();
                } else {
                    setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }

    /**
     * There is a bug when applying a {@link android.graphics.ColorFilter} to the {@link ImageView}, most likely caused by the requirement to {@link
     * Drawable#mutate()}
     * the drawable. We need to clear the ImageView's ColorFilter, and instead apply the ColorFilter directly to the drawable.
     *
     * @param drawable Drawable layer.
     */
    private void applyColorFilterToLayerDrawable(LayerDrawable drawable) {
        if (mTintColorList != null) {
            clearColorFilter();
            int color = mTintColorList.getColorForState(getDrawableState(), Color.TRANSPARENT);

            if (color == Color.TRANSPARENT) {
                drawable.clearColorFilter();
            } else {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    /**
     * @return the LayerDrawable used to wrap content, if any
     */
    @SuppressWarnings("UnusedDeclaration")
    public LayerDrawable getLayerDrawable() {
        return mLayerDrawable;
    }

    /**
     * Set the layer drawable and the id of the layer within the drawable to use for content
     *
     * @param layerDrawable Drawable layer.
     * @param contentLayerId Content layer id.
     */
    public void setLayerResources(LayerDrawable layerDrawable, int contentLayerId) {
        if (layerDrawable != null && contentLayerId == CONTENT_LAYER_ID_EMPTY) {
            throw new IllegalArgumentException("Cannot use a selector drawable without specifying a selector layer id");
        }

        mContentLayerId = contentLayerId;
        mLayerDrawable = layerDrawable;

        updateDrawable(mContentDrawable);
    }

    /**
     * Sets the scale type to use when drawing content
     *
     * @param scaleType Scale type.
     */
    public void setScaleType(PlusScaleType scaleType) {
        mScaleType = scaleType;

        if (scaleType.superScaleType != null) {
            // Use the scaling built in to the ImageView for any scale type which has a super scale type
            setScaleType(scaleType.superScaleType);
        } else {
            // Our own built-in scale types all use a custom MATRIX implementation
            setScaleType(ImageView.ScaleType.MATRIX);
        }
    }

    /**
     * @param listener the DefaultDrawableListener to set
     */
    @SuppressWarnings("UnusedDeclaration")
    public void setListener(final DefaultDrawableListener listener) {
        this.mListener = listener;
    }

    /**
     * @return the DefaultDrawableListener
     */
    @SuppressWarnings("UnusedDeclaration")
    public DefaultDrawableListener getListener() {
        return mListener;
    }

    /**
     * Returns true if this view is referencing a BitmapDrawable, and its Bitmap reference has been recycled.
     *
     * @return Has drawable been recycled.
     */
    public boolean isDrawableRecycled() {
        final Drawable drawable = getDrawable();

        if (drawable instanceof BitmapDrawable) {
            final Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();

            if (bmp != null) {
                return bmp.isRecycled();
            }
        } else if (drawable instanceof LayerDrawable) {
            // This is a somewhat shoddy implementation, but it's quick and gets the job done.
            final LayerDrawable layerDrawable = (LayerDrawable) drawable;

            // Iterate over the layers; look for a recycled bitmap
            for (int i = 0; i < (layerDrawable).getNumberOfLayers(); i++) {
                final Drawable iDrawable = layerDrawable.getDrawable(i);

                if (iDrawable instanceof BitmapDrawable) {
                    final BitmapDrawable bitmapDrawable = (BitmapDrawable) iDrawable;
                    final Bitmap bmp = bitmapDrawable.getBitmap();

                    if (bmp != null) {
                        if (bmp.isRecycled()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Resets the ImageView resource back to the default.
     */
    public void resetToDefault() {
        setImageDrawable(mDefaultDrawable);
        refreshDrawableState();
    }

    /**
     * Returns the view's default drawable.
     *
     * @return current default drawable

     * @see #setDefaultDrawable(Drawable)
     */
    public Drawable getDefaultDrawable() {
        return mDefaultDrawable;
    }

    /**
     * Returns <code>true</code> if the view is currently referencing the default drawable. If no default has been provided, this method returns
     * false, even if there is no super-class drawable (i.e., even if both Drawables are null, this method still returns false).
     * <p/>
     * <b>Warning</b>: If both attributes refer to the same resource, this will return <code>false</code>. That is because
     * <code>android:src="@drawable/foo"</code> and <code>app:defaultDrawable="@drawable/foo"</code> both create new (distinct) Drawable instances,
     * therefore they are not equal.
     *
     * @return Is the default drawable showing.
     */
    public boolean isShowingDefaultDrawable() {
        return mDefaultDrawable != null && mDefaultDrawable == getDrawable();
    }

    /**
     * Sets a drawable as the default drawable for the view.
     *
     * @param drawable The drawable to set
     * @see #getDefaultDrawable()
     */
    public void setDefaultDrawable(final Drawable drawable) {
        final boolean wasDefault = isShowingDefaultDrawable();

        if (mDefaultDrawable != null) {
            mDefaultDrawable.setCallback(null);
            unscheduleDrawable(mDefaultDrawable);
        }

        mDefaultDrawable = drawable;

        if (wasDefault || getDrawable() == null) {
            setImageDrawable(drawable);
        }
    }

    /**
     * Sets a resource id as the default drawable for the view.
     *
     * @param resId The desired resource identifier, as generated by the aapt tool. This integer encodes the package, type, and resource entry. The
     * value 0 is an invalid identifier.
     *
     * @see #setDefaultDrawable(Drawable)
     */
    @SuppressWarnings("unused")
    public void setDefaultResource(final int resId) {
        setDefaultDrawable(getResources().getDrawable(resId));
    }

    /**
     * Overrides the built-in ImageView onDraw() behavior, in order to intercept a recycled Bitmap, which otherwise causes a crash. If the Bitmap is
     * recycled, this performs a last-minute replacement using the default drawable.
     */
    @Override
    protected void onDraw(@NonNull final Canvas canvas) {
        if (isDrawableRecycled()) {
            performAutoResetDefaultDrawable();
        }

        super.onDraw(canvas);
    }

    /**
     * Performs the automatic reset to the default drawable. This will also dispatch the
     * {@link DefaultDrawableListener#onAutoResetDefaultDrawable(BlitzImageViewPlus)} callback.
     *
     * @see BlitzImageViewPlus#resetToDefault()
     */
    protected void performAutoResetDefaultDrawable() {
        resetToDefault();

        if (mListener != null) {
            mListener.onAutoResetDefaultDrawable(this);
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        if (PlusScaleType.TOP_CROP.equals(mScaleType)) {
            Drawable drawable = super.getDrawable();

            if (drawable != null) {
                float floatLeft = (float) l;
                float floatRight = (float) r;

                float intrinsicWidth = drawable.getIntrinsicWidth();
                float intrinsicHeight = drawable.getIntrinsicHeight();

                int frameHeight = b - t;

                if (intrinsicWidth != -1) {
                    float scaleFactor = (floatRight - floatLeft) / intrinsicWidth;

                    if (scaleFactor * intrinsicHeight < frameHeight) {
                        setScaleType(ScaleType.CENTER_CROP);
                    } else {
                        setScaleType(ScaleType.MATRIX);
                        Matrix matrix = new Matrix();
                        // scale width
                        matrix.setScale(scaleFactor, scaleFactor);
                        setImageMatrix(matrix);
                    }
                }
            }
        }

        return super.setFrame(l, t, r, b);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private void resolveUri() {
        if (mContentDrawable != null && mContentDrawable != mDefaultDrawable) {
            return;
        }

        Resources rsrc = getResources();
        if (rsrc == null) {
            return;
        }

        Drawable d = null;

        if (mContentResource != 0) {
            try {
                d = rsrc.getDrawable(mContentResource);
            } catch (Exception e) {
                // Don't try again.
                mContentUri = null;
            }
        } else if (mContentUri != null) {
            String scheme = mContentUri.getScheme();

            if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {

                /*
                 * FIXME Can't be used due to missing compilation unit for ContentResolver#getResourceId
                 */

                // try {
                // getContext().getResources().get
                // // Load drawable through Resources, to get the source density information
                // ContentResolver.OpenResourceIdResult r = getContext().getContentResolver().getResourceId(mContentUri);
                // getContext().getContentResolver().d = r.r.getDrawable(r.id);
                // } catch (Exception e) {
                // Log.w("ImageView", "Unable to open content: " + mUri, e);
                // }
            } else if (ContentResolver.SCHEME_CONTENT.equals(scheme) || ContentResolver.SCHEME_FILE.equals(scheme)) {
                try {
                    d = Drawable.createFromStream(getContext().getContentResolver().openInputStream(mContentUri), null);
                } catch (Exception e) {
                    Log.w("ImageView", "Unable to open content: " + mContentUri, e);
                }
            } else {
                d = Drawable.createFromPath(mContentUri.toString());
            }

            if (d == null) {
                System.out.println("resolveUri failed on bad bitmap uri: " + mContentUri);
                // Don't try again.
                mContentUri = null;
            }
        } else {
            return;
        }

        updateDrawable(d);
    }

    /**
     * Updates the ImageView to show the given {@link Drawable} as content, updating the selector drawable layer if one is provided
     *
     * @param drawable The drawable to set
     */
    private void updateDrawable(Drawable drawable) {
        if (drawable == null && mDefaultDrawable != null) {
            drawable = mDefaultDrawable;
        }

        mContentDrawable = drawable;

        if (mLayerDrawable != null) {
            applyColorFilterToLayerDrawable(mLayerDrawable);

            if (drawable == null) {
                // We need a placeholder drawable for the LayerDrawable
                drawable = new ShapeDrawable(new RectShape());
            }

            // FIXME there is probably a better workaround for doing this to include re-requesting layout if the content drawable is new and not
            // equal-dimension
            super.setImageDrawable(null);

            // Selector is there, find the id in the layer and update that layer's Drawable
            mLayerDrawable.setDrawableByLayerId(mContentLayerId, drawable);
            super.setImageDrawable(mLayerDrawable);
        } else {
            if (drawable instanceof LayerDrawable) {
                applyColorFilterToLayerDrawable((LayerDrawable) drawable);
            }

            super.setImageDrawable(drawable);
        }
    }

    /**
     * <code>
     * <enum name="matrix" value="0" />
     * <enum name="fitXY" value="1" />
     * <enum name="fitStart" value="2" />
     * <enum name="fitCenter" value="3" />
     * <enum name="fitEnd" value="4" />
     * <enum name="center" value="5" />
     * <enum name="centerCrop" value="6" />
     * <enum name="centerInside" value="7" />
     * <enum name="top_crop" value="8" />
     * </code>
     *
     * @see ImageView.ScaleType
     * @see R.attr#scaleType
     */
    public static enum PlusScaleType {
        /**
         * Scale using the image matrix when drawing. The image matrix can be set using {@link ImageView#setImageMatrix(Matrix)}. From XML, use this
         * syntax: <code>android:scaleType="matrix"</code>.
         */
        MATRIX(ImageView.ScaleType.MATRIX, 0),
        /**
         * Scale the image using {@link Matrix.ScaleToFit#FILL}. From XML, use this syntax: <code>android:scaleType="fitXY"</code>.
         */
        FIT_XY(ImageView.ScaleType.FIT_XY, 1),
        /**
         * Scale the image using {@link Matrix.ScaleToFit#START}. From XML, use this syntax: <code>android:scaleType="fitStart"</code>.
         */
        FIT_START(ImageView.ScaleType.FIT_START, 2),
        /**
         * Scale the image using {@link Matrix.ScaleToFit#CENTER}. From XML, use this syntax: <code>android:scaleType="fitCenter"</code>.
         */
        FIT_CENTER(ImageView.ScaleType.FIT_CENTER, 3),
        /**
         * Scale the image using {@link Matrix.ScaleToFit#END}. From XML, use this syntax: <code>android:scaleType="fitEnd"</code>.
         */
        FIT_END(ImageView.ScaleType.FIT_END, 4),
        /**
         * Center the image in the view, but perform no scaling. From XML, use this syntax: <code>android:scaleType="center"</code>.
         */
        CENTER(ImageView.ScaleType.CENTER, 5),
        /**
         * Scale the image uniformly (maintain the image's aspect ratio) so that both dimensions (width and height) of the image will be equal to or
         * larger than the corresponding dimension of the view (minus padding). The image is then centered in the view. From XML, use this syntax:
         * <code>android:scaleType="centerCrop"</code>.
         */
        CENTER_CROP(ImageView.ScaleType.CENTER_CROP, 6),
        /**
         * Scale the image uniformly (maintain the image's aspect ratio) so that both dimensions (width and height) of the image will be equal to or
         * less than the corresponding dimension of the view (minus padding). The image is then centered in the view. From XML, use this syntax:
         * <code>android:scaleType="centerInside"</code>.
         */
        CENTER_INSIDE(ImageView.ScaleType.CENTER_INSIDE, 7),
        /**
         * Scale the image uniformly (maintain the image's aspect ratio) so that the width of the image fits the width of the ImageView, but the top
         * of the image is aligned with the top of the ImageView, cropping any excess from the bottom. From XML, use this syntax:
         * <code>android:scaleType="top_crop"</code>.
         */
        TOP_CROP(null, 8);

        public final ImageView.ScaleType superScaleType;
        public final int nativeInt;

        private final static SparseArray<PlusScaleType> sScaleTypes = new SparseArray<PlusScaleType>();

        static {
            for (PlusScaleType scaleType : values()) {
                sScaleTypes.put(scaleType.nativeInt, scaleType);
            }
        }

        public static PlusScaleType getScaleType(int nativeInt) {
            if (nativeInt >= 0) {
                return sScaleTypes.get(nativeInt);
            }

            return null;
        }

        private PlusScaleType(ImageView.ScaleType originType, int nativeInt) {
            this.superScaleType = originType;
            this.nativeInt = nativeInt;
        }
    }

    /**
     * Interface definition for callbacks to be invoked when ImageViewPlus events occur.
     */
    public interface DefaultDrawableListener {
        /**
         * Called when the view's Bitmap is found to be recycled JIT in onDraw(). The ImageView has already been reset back to the default view by
         * this point.
         *
         * @param view The view that had its drawable reset back to the default
         */
        public void onAutoResetDefaultDrawable(final BlitzImageViewPlus view);
    }
}
