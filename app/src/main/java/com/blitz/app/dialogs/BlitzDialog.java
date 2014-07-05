package com.blitz.app.dialogs;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blitz.app.R;
import com.blitz.app.utilities.reflection.ReflectionHelper;

/**
 * Created by Miguel Gaeta on 6/29/14.
 */
public class BlitzDialog {

    // Dialog is represented by a popup window.
    PopupWindow mPopupWindow;

    // Parent activity.
    Activity mActivity;

    /**
     * Do not allow empty constructor.
     */
    @SuppressWarnings("unused")
    private BlitzDialog() {

    }

    public BlitzDialog(Activity activity) {


        // Use reflection to fetch the associated view resource id and set content view.
        int layoutResourceId = ReflectionHelper.getResourceId(this.getClass(), R.layout.class);

        View layoutOfPopup = activity.getLayoutInflater().inflate(layoutResourceId, null);

        mActivity = activity;

        mPopupWindow = new PopupWindow(layoutOfPopup,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void show() {
        mPopupWindow.showAtLocation(mActivity.getWindow().getDecorView(),
                Gravity.NO_GRAVITY, 0, 0);
    }

    public void hide() {
        mPopupWindow.dismiss();
    }

    protected void setTouchable(boolean touchable) {

        mPopupWindow.setBackgroundDrawable(touchable ? new ColorDrawable(Color.TRANSPARENT) : null);
        mPopupWindow.setOutsideTouchable(touchable);
        mPopupWindow.setTouchable(touchable);
        mPopupWindow.setFocusable(touchable);
    }
}