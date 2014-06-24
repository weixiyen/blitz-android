package com.blitz.app.screens.post;

import com.blitz.app.R;
import com.blitz.app.base.activity.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/1/14.
 */
public class PostScreenActivity extends BaseActivity {

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Register user and take them to main screen.
     */
    @OnClick(R.id.post_screen_back) @SuppressWarnings("unused")
    public void postFavor() {

        finish();
    }
}