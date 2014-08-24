package com.blitz.app.dialogs.info;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseDialog;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import butterknife.InjectView;

/**
 * Created by Miguel Gaeta on 6/4/14. Copyright 2014 Blitz Studios
 */
public class DialogInfo extends BaseDialog {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.dialog_info_text)       TextView mInfoText;
    @InjectView(R.id.dialog_info_left_button)  Button mInfoLButton;
    @InjectView(R.id.dialog_info_right_button) Button mInfoRButton;

    private LinearLayout.LayoutParams mButtonLp;
    private LinearLayout.LayoutParams mButtonLpSmall;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public DialogInfo(Activity activity) {
        super(activity);

        // Hide all elements on load.
        mInfoText   .setVisibility(View.GONE);
        mInfoLButton.setVisibility(View.GONE);
        mInfoRButton.setVisibility(View.GONE);

        // Get pixel values of button dimensions.
        int buttonHeight     = ReflectionHelper.densityPixelsToPixels(mActivity, 60);
        int buttonWidth      = ReflectionHelper.densityPixelsToPixels(mActivity, 295);
        int buttonWidthSmall = ReflectionHelper.densityPixelsToPixels(mActivity, 145);

        // Create supported button sizes.
        mButtonLp      = new LinearLayout.LayoutParams(buttonWidth,      buttonHeight);
        mButtonLpSmall = new LinearLayout.LayoutParams(buttonWidthSmall, buttonHeight);

        // Can touch and dismiss.
        setTouchable(true);
        setDismissible(true);
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Set info text of dialog.
     *
     * @param resourceId Text.
     */
    @SuppressWarnings("unused")
    public void setInfoText(int resourceId) {

        mInfoText.setText(resourceId);
        mInfoText.setVisibility(View.VISIBLE);
    }

    /**
     * Setup left button of info dialog.
     *
     * @param resourceId Text.
     * @param clickAction Click action.
     */
    @SuppressWarnings("unused")
    public void setInfoLeftButton(int resourceId, final Runnable clickAction) {

        mInfoLButton.setText(resourceId);
        mInfoLButton.setVisibility(View.VISIBLE);
        mInfoLButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickAction != null) {
                    clickAction.run();
                }
            }
        });

        // Refresh dimensions.
        setButtonDimensions();
    }

    /**
     * Setup right button of info dialog.
     *
     * @param resourceId Text.
     * @param clickAction Click action.
     */
    @SuppressWarnings("unused")
    public void setInfoRightButton(int resourceId, final Runnable clickAction) {

        mInfoRButton.setText(resourceId);
        mInfoRButton.setVisibility(View.VISIBLE);
        mInfoRButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (clickAction != null) {
                    clickAction.run();
                }
            }
        });

        // Refresh dimensions.
        setButtonDimensions();
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Based on current visibility status, set buttons to either
     * normal dimensions (full width) or half width size.
     */
    private void setButtonDimensions() {

        int lButtonVisibility = mInfoLButton.getVisibility();
        int rButtonVisibility = mInfoRButton.getVisibility();

        if (lButtonVisibility == View.VISIBLE &&
            rButtonVisibility == View.VISIBLE) {

            mInfoLButton.setBackgroundResource(R.drawable.drawable_button_dialog_small);
            mInfoRButton.setBackgroundResource(R.drawable.drawable_button_dialog_small);

            mInfoLButton.setLayoutParams(mButtonLpSmall);
            mInfoRButton.setLayoutParams(mButtonLpSmall);
        } else {

            mInfoLButton.setBackgroundResource(R.drawable.drawable_button_dialog);
            mInfoRButton.setBackgroundResource(R.drawable.drawable_button_dialog);

            mInfoLButton.setLayoutParams(mButtonLp);
            mInfoRButton.setLayoutParams(mButtonLp);
        }
    }
}