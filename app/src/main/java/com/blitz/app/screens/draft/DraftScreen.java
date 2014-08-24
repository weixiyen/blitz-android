package com.blitz.app.screens.draft;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.view_models.ViewModelDraft;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/27/14.
 */
public class DraftScreen extends BaseActivity implements ViewModelDraft.ViewModelDraftCallbacks {

    @InjectView(R.id.draft_intro) ViewGroup mDraftIntroContainer;
    @InjectView(R.id.draft_loading) ProgressBar mDraftLoadingSpinner;

    @InjectView(R.id.draft_header) TextView mDraftHeader;

    /**
     * When screen is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the view model.
        setViewModel(new ViewModelDraft(), savedInstanceState);
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

    //==============================================================================================
    // View Model Callbacks
    //==============================================================================================

    @Override
    public void onDraftingStarted() {

        // TODO: Cool animations.
        mDraftIntroContainer.setVisibility(View.GONE);
        mDraftLoadingSpinner.setVisibility(View.GONE);
    }
}
