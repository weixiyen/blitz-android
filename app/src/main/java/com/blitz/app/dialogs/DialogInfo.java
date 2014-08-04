package com.blitz.app.dialogs;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseDialog;

import butterknife.InjectView;

/**
 * Created by Miguel Gaeta on 6/4/14.
 */
public class DialogInfo extends BaseDialog {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.dialog_info_text)       TextView mInfoText;
    @InjectView(R.id.dialog_info_left_button)  Button mInfoLButton;
    @InjectView(R.id.dialog_info_right_button) Button mInfoRButton;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    public DialogInfo(Activity activity) {
        super(activity);

        // Hide all elements on load.
        mInfoText   .setVisibility(View.GONE);
        mInfoLButton.setVisibility(View.GONE);
        mInfoRButton.setVisibility(View.GONE);

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
    }
}