package com.blitz.app.utilities.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.blitz.app.R;

/**
 * Created by mrkcsc on 9/18/14. Copyright 2014 Blitz Studios
 */
public class BlitzImageView extends ImageView {

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
                (attrs, R.styleable.BlitzTextView);

        setupClearDefault(styledAttributes);

        // Done reading attributes.
        styledAttributes.recycle();
    }

    @SuppressWarnings("unused")
    public BlitzImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Fetch custom attributes.
        TypedArray styledAttributes = getContext().obtainStyledAttributes
                (attrs, R.styleable.BlitzTextView, defStyle, 0);

        setupClearDefault(styledAttributes);

        // Done reading attributes.
        styledAttributes.recycle();
    }

    // endregion

    // region Private Methods
    // =============================================================================================

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