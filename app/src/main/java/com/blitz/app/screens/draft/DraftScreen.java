package com.blitz.app.screens.draft;

import android.os.Bundle;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/27/14.
 */
public class DraftScreen extends BaseActivity {

    @InjectView(R.id.draft_header) TextView mDraftHeader;

    /**
     * When screen is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * Runs when activity has been
     * presented to the user.
     */
    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Run custom transitions if needed, also
     * start timer to detect entering background.
     */
    @Override
    protected void onPause() {
        super.onPause();

    }
}
