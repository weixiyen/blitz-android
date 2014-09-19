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

    // region Member Variables
    // =============================================================================================

    private boolean ignoreTextChanges;

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

        if (ignoreTextChanges) {
            ignoreTextChanges = false;

            return;
        }

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

    /**
     * If set, clear out any text set on this text view.
     * Allows the developer to have good previews of
     * the XML but not have it have any sort of flicker
     * when loading the real text from the network.
     *
     * @param styledAttributes Custom attributes.
     */
    private void setupClearDefault(TypedArray styledAttributes) {

        if (!isInEditMode() && styledAttributes.hasValue(R.styleable.BlitzTextView_clearDefault)) {

            boolean clearDefault = styledAttributes.getBoolean
                    (R.styleable.BlitzTextView_clearDefault, false);

            if (clearDefault) {

                ignoreTextChanges = true;

                setText(null);
            }
        }
    }

    // endregion
}