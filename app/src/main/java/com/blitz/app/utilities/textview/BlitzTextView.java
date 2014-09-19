package com.blitz.app.utilities.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.blitz.app.R;
import com.blitz.app.utilities.logging.LogHelper;

import me.grantland.widget.AutofitTextView;

/**
 * Created by Miguel on 9/7/2014. Copyright 2014 Blitz Studios
 */
public class BlitzTextView extends AutofitTextView {

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

        // Done reading attributes.
        styledAttributes.recycle();
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    @Override
    protected void onTextChanged(java.lang.CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        // If text changed in some way.
        if (lengthBefore != 0 || lengthAfter != 0) {

            LogHelper.log("Changed it bruh: " + getId() + " | '" + text + "' " + start + " " + lengthBefore + " " + lengthAfter);
        }
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    private void setupCachedText(TypedArray styledAttributes) {

        if (!isInEditMode() && styledAttributes.hasValue(R.styleable.BlitzTextView_cacheText)) {

            boolean cacheText = styledAttributes.getBoolean
                    (R.styleable.BlitzTextView_cacheText, false);

            // Can only cache if resId is set.
            if (cacheText && getId() != -1) {


                LogHelper.log("Restore cache bruh");

                setText("");
            }
        }
    }

    // endregion
}