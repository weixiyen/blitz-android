package com.blitz.app.screens.main;

import android.content.Intent;

import com.blitz.app.R;
import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.screens.post.PostScreenActivity;

import butterknife.OnClick;

public class MainScreenActivity extends BaseActivity {

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Start next activity in the flow.
     */
    public void transitionNextScreen() {

        // Create intent for main screen activity.
        Intent intent = new Intent(this, PostScreenActivity.class);

        startActivity(intent);
    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    /**
     * Register user and take them to main screen.
     */
    @OnClick(R.id.main_screen_post) @SuppressWarnings("unused")
    public void postFavor() {

        transitionNextScreen();
    }
}