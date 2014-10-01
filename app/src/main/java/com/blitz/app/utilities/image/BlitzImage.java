package com.blitz.app.utilities.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;

import com.blitz.app.utilities.app.AppConfig;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mrkcsc on 9/30/14. Copyright 2014 Blitz Studios
 *
 * Handles loading image urls from the network.
 */
public class BlitzImage {

    // region Image URL Methods
    // =============================================================================================

    // Picasso targets are weak, so we need to hold onto them.
    private static ConcurrentHashMap<Integer, Target> mTargets =
            new ConcurrentHashMap<Integer, Target>();

    // endregion

    // region Image URL Methods
    // =============================================================================================

    /**
     * Load an image url.
     *
     * @param context Context.
     * @param imageUrl Image url.
     * @param callback Callback fired when image loaded.
     */
    @SuppressWarnings("unused")
    public static void loadImageUrl(Context context, String imageUrl,
                                    CallbackImageUrl callback) {

        // Call single load, with no mask.
        loadImageUrl(context, imageUrl, null, callback);
    }

    /**
     * Load a single image with optional mask
     * to go with it.
     *
     * @param context Context.
     * @param imageUrl Image url.
     * @param maskAssetUrl Asset to mask.
     * @param callback Callback fired when image loaded.
     */
    @SuppressWarnings("unused")
    public static void loadImageUrl(Context context, String imageUrl, String maskAssetUrl,
                                    final CallbackImageUrl callback) {

        // Create a list with out image url.
        List<String> imageUrls = new ArrayList<String>();

        imageUrls.add(imageUrl);

        // Create a list with one asset.
        List<String> maskAssetUrls = new ArrayList<String>();

        maskAssetUrls.add(maskAssetUrl);

        // Now call multiple load.
        loadImageUrls(context, imageUrls, maskAssetUrls, new CallbackImageUrls() {

            @Override
            public void onSuccess(List<Bitmap> images) {

                if (callback != null) {
                    callback.onSuccess(images.get(0));
                }
            }
        });
    }

    /**
     * Load a list of image urls.
     *
     * @param context Context.
     * @param imageUrls List of urls.
     * @param callback Callback fired when images loaded.
     */
    @SuppressWarnings("unused")
    public static void loadImageUrls(Context context, List<String> imageUrls,
                                     CallbackImageUrls callback) {

        // Call multiple load, with no masks.
        loadImageUrls(context, imageUrls, new ArrayList<String>(imageUrls.size()), callback);
    }

    /**
     * Load image urls with a shared mask asset.
     *
     * @param context Context.
     * @param imageUrls Array of image urls we are loading.
     * @param maskAssetUrl Asset to mask.
     * @param callback Callback fired when images loaded.
     */
    @SuppressWarnings("unused")
    public static void loadImageUrls(Context context, List<String> imageUrls,
                                     String maskAssetUrl, CallbackImageUrls callback) {

        // Need at least one image url.
        if (imageUrls == null || imageUrls.size() > 0) {

            return;
        }

        // Create a list of masked asset urls.
        List<String> maskAssetUrls = new ArrayList<String>();

        for (String ignored : imageUrls) {

            maskAssetUrls.add(maskAssetUrl);
        }

        // Call multiple load, with no masks.
        loadImageUrls(context, imageUrls, maskAssetUrls, callback);
    }

    /**
     * Load a series of image urls with optional
     * series of masks to go with it.
     *
     * @param context Context.
     * @param imageUrls List of urls.
     * @param maskAssetUrls List of assets to mask.
     * @param callback Callback fired when images loaded.
     */
    @SuppressWarnings("unused")
    public static void loadImageUrls(Context context,
                                     final List<String> imageUrls,
                                     final List<String> maskAssetUrls,
                                     final CallbackImageUrls callback) {

        // Need at least one url.
        if (imageUrls == null) {

            return;
        }

        // Result images.
        final List<Bitmap> images = new ArrayList<Bitmap>();

        for (int i = 0; i < imageUrls.size(); i++) {

            RequestCreator requestCreator = Picasso.with(context)
                    .load(AppConfig.getCDNUrl() + imageUrls.get(i));

            // Add a masking transform if a url exists for it.
            if (maskAssetUrls != null && maskAssetUrls.get(i) != null) {

                // Add mask if provided.
                requestCreator = requestCreator.transform
                        (getMaskAssetTransformation(context, maskAssetUrls.get(i)));
            }

            // Create a target to handle the loaded image urls.
            Target imageUrlTarget = fetchImageUrlTarget(images, imageUrls, callback);

            // Place into targets map.
            mTargets.put(System.identityHashCode(imageUrlTarget), imageUrlTarget);

            // Load into target.
            requestCreator.into(imageUrlTarget);
        }
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Create a callback target for loading an image
     * into some array of images. As part of a
     * larger list of image urls.
     *
     * @param images Array of loaded bitmaps to add to.
     * @param imageUrls Array of image urls we are loading.
     *
     * @param callback Callback.
     *
     * @return Picasso target class (load images into it).
     */
    private static Target fetchImageUrlTarget(final List<Bitmap> images,
                                              final List<String> imageUrls,
                                              final CallbackImageUrls callback) {

        return new Target() {

            /**
             * When bitmap is loaded, try to add it to images
             * array and if all image urls are loaded,
             * we then execute the callback.
             *
             * @param bitmap Loaded bitmap.
             * @param from Loaded from.
             */
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                images.add(bitmap);

                // If all images have been added.
                if (images.size() == imageUrls.size()) {

                    if (callback != null) {
                        callback.onSuccess(images);
                    }
                }

                // Now done with this target.
                mTargets.remove(System.identityHashCode(this));
            }

            @Override
            public void onBitmapFailed
                    (Drawable errorDrawable) {

                // Now done with this target.
                mTargets.remove(System.identityHashCode(this));
            }

            @Override
            public void onPrepareLoad
                    (Drawable placeHolderDrawable) { }
        };
    }

    /**
     * Fetch a bitmap mask from an asset URL.  This
     * will not apply any sort of scaling or DPI
     * calculations, the bitmap will be raw pixel size.
     *
     * @param maskAssetUrl Asset url.
     *
     * @return Decoded bitmap.
     */
    private static Bitmap getMaskAsset(Context context, String maskAssetUrl) {

        InputStream inputStream = null;

        try {

            // Attempt to fetch input stream for asset.
            inputStream = context.getAssets().open(maskAssetUrl);

        } catch (IOException ignored) { }

        return BitmapFactory.decodeStream(inputStream);
    }

    /**
     * Fetch a transformer to mask an image bitmap
     * source with some arbitrary image mask.
     *
     * @param maskAssetUrl Asset url.
     *
     * @return Transformation.
     */
    private static Transformation getMaskAssetTransformation(final Context context,
                                                             final String maskAssetUrl) {

        return new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {

                Bitmap sourceMask = getMaskAsset(context, maskAssetUrl);

                // Create a result bitmap of same size as mask.
                Bitmap result = Bitmap.createBitmap(source.getWidth(),
                        source.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas mCanvas = new Canvas(result);

                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

                mCanvas.drawBitmap(source, 0, 0, null);

                if (sourceMask != null) {

                    // Apply the mask if it exists.
                    mCanvas.drawBitmap(sourceMask, 0, 0, paint);
                }

                paint.setXfermode(null);

                // Cleanup.
                source.recycle();

                if (sourceMask != null) {
                    sourceMask.recycle();
                }

                return result;
            }

            @Override
            public String key() {

                return "blitz()";
            }
        };
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    /**
     * Callback when image url is loaded.
     */
    public interface CallbackImageUrl {

        // Single bitmap.
        public void onSuccess(Bitmap image);
    }

    /**
     * Callback when image urls are loaded.
     */
    public interface CallbackImageUrls {

        // List of bitmaps.
        public void onSuccess(List<Bitmap> images);
    }

    // endregion
}