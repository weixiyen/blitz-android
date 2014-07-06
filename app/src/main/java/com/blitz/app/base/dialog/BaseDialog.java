package com.blitz.app.base.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blitz.app.R;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import butterknife.ButterKnife;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class BaseDialog {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Dialog is represented by a popup window.
    PopupWindow mPopupWindow;

    // Parent activity.
    Activity mActivity;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Do not allow empty constructor.
     */
    @SuppressWarnings("unused")
    private BaseDialog() {

    }

    /**
     * Default constructor.  Sets up a dialog
     * using project defaults.
     *
     * @param activity Associated activity.
     */
    public BaseDialog(Activity activity) {

        // Use reflection to fetch the associated view resource id and set content view.
        int layoutResourceId = ReflectionHelper.getResourceId(this.getClass(), R.layout.class);

        // Now inflate the associated view.
        View layoutOfPopup = activity.getLayoutInflater().inflate(layoutResourceId, null);

        // Save the activity.
        mActivity = activity;

        // Create full size popup window.
        mPopupWindow = new PopupWindow(layoutOfPopup,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        // When the popup is dismissed, reset any butter-knife injections.
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                // Probably not needed, but good practice.
                ButterKnife.reset(this);
            }
        });

        // Inject butter knife into the content view.
        ButterKnife.inject(this, mPopupWindow.getContentView());
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Show the popup.
     */
    public void show() {

        // Show at top corner of the window.
        mPopupWindow.showAtLocation(mActivity.getWindow().getDecorView(),
                Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * Hide the popup.
     */
    public void hide() {
        mPopupWindow.dismiss();
    }

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

    /**
     * Set whether this dialog can be touched.  Only useable
     * by direct dialog subclasses.
     *
     * @param touchable Is dialog touchable.
     */
    @SuppressWarnings("unused")
    protected void setTouchable(boolean touchable) {

        mPopupWindow.setOutsideTouchable(touchable);
        mPopupWindow.setTouchable(touchable);
        mPopupWindow.setFocusable(touchable);
    }

    /**
     * Popup is only dismissible if you provide
     * a background drawable. http://bit.ly/1oaUSSy
     *
     * @param dismissible Boolean toggle.
     */
    @SuppressWarnings("unused")
    protected void setDismissible(boolean dismissible) {

        // Provide a drawable if dismissible.
        mPopupWindow.setBackgroundDrawable
                (dismissible ? new ColorDrawable(Color.TRANSPARENT) : null);
    }
}