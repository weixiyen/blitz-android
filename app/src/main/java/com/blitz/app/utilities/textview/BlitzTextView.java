package com.blitz.app.utilities.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.blitz.app.R;
import com.blitz.app.utilities.app.AppDataObject;

import java.util.HashMap;

import me.grantland.widget.AutofitTextView;

/**
 * Created by Miguel on 9/7/2014. Copyright 2014 Blitz Studios
 */
public class BlitzTextView extends AutofitTextView {

    // region Member Variables
    // =============================================================================================

    // Should text changed be ignored.
    private boolean mIgnoreTextChanges;

    // Should cache the text.
    private boolean mCacheText;

    // endregion

    // region Constructors
    // =============================================================================================

    @SuppressWarnings("unused")
    public BlitzTextView(Context context) {
        super(context);
    }

    @SuppressWarnings("unused")
    public BlitzTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Fetch custom attributes.
        TypedArray styledAttributes = getContext().obtainStyledAttributes
                (attrs, R.styleable.BlitzTextView);

        setupCachedText(styledAttributes);
        setupClearDefault(styledAttributes);

        // Done reading attributes.
        styledAttributes.recycle();
    }

    @SuppressWarnings("unused")
    public BlitzTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Fetch custom attributes.
        TypedArray styledAttributes = getContext().obtainStyledAttributes
                (attrs, R.styleable.BlitzTextView, defStyle, 0);

        setupCachedText(styledAttributes);
        setupClearDefault(styledAttributes);

        // Done reading attributes.
        styledAttributes.recycle();
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    @Override
    protected void onTextChanged(java.lang.CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (mIgnoreTextChanges) {
            mIgnoreTextChanges = false;

            return;
        }

        // If text changed in some way.
        if (lengthBefore != 0 || lengthAfter != 0) {

            // Update cache if needed.
            setCachedText(text.toString());
        }
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Set text to the current cache value, if present.
     */
    private void getCachedText() {

        // Can only cache if resource id is set.
        if (mCacheText && getId() != -1) {

            HashMap<String, String> cachedText = AppDataObject.cachedText.get();

            if (cachedText.containsKey(getResources().getResourceName(getId()))) {

                mIgnoreTextChanges = true;

                setText(cachedText.get(getResources().getResourceName(getId())));
            }
        }
    }

    /**
     * Cache the specified text string.
     *
     * @param text Text to cache.
     */
    private void setCachedText(String text) {

        if (mCacheText && getId() != -1) {

            HashMap<String, String> cachedText = AppDataObject.cachedText.get();

            // Update dictionary with new text.
            cachedText.put(getResources().getResourceName(getId()), text);

            AppDataObject.cachedText.set(cachedText);
        }
    }

    /**
     * Setup text caching.
     *
     * @param styledAttributes Custom attributes.
     */
    private void setupCachedText(TypedArray styledAttributes) {

        if (!isInEditMode() && styledAttributes.hasValue(R.styleable.BlitzTextView_cacheText)) {

            mCacheText = styledAttributes.getBoolean
                    (R.styleable.BlitzTextView_cacheText, false);

            getCachedText();
        }
    }

    /**
     * If set, clear out any text set on this text view.
     *
     * Allows the developer to have good previews of
     * the XML but not have it have any sort of flicker
     * when loading the real text from the network.
     *
     * @param styledAttributes Custom attributes.
     */
    private void setupClearDefault(TypedArray styledAttributes) {

        if (!isInEditMode() && styledAttributes.hasValue(R.styleable.BlitzTextView_clearInitialContent)) {

            boolean clearDefault = styledAttributes.getBoolean
                    (R.styleable.BlitzTextView_clearInitialContent, false);

            if (clearDefault) {

                mIgnoreTextChanges = true;

                setText(null);
            }
        }
    }

    // endregion
}