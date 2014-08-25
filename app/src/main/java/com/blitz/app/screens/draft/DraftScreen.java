package com.blitz.app.screens.draft;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivityViewModel;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelDraft;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 7/27/14. Copyright 2014 Blitz Studios
 */
public class DraftScreen extends BaseActivityViewModel implements ViewModelDraft.ViewModelDraftCallbacks {

    @InjectView(R.id.draft_intro)   ViewGroup mDraftContainerIntro;

    @InjectView(R.id.draft_container_drafting) ViewGroup mDraftContainerDrafting;

    @InjectView(R.id.draft_loading) ProgressBar mDraftLoadingSpinner;
    @InjectView(R.id.draft_header) TextView mDraftHeader;

    // View model object.
    private ViewModelDraft mViewModelDraft;

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

    /**
     * This method requests an instance of the view
     * model to operate on for lifecycle callbacks.
     *
     * @return Instantiated instance of the view model
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModelDraft == null) {
            mViewModelDraft = new ViewModelDraft();
        }

        return mViewModelDraft;
    }

    //==============================================================================================
    // View Model Callbacks
    //==============================================================================================

    @Override
    public void onDraftingStarted() {

        // Hide the intro UI.
        mDraftContainerIntro.setVisibility(View.GONE);
        mDraftLoadingSpinner.setVisibility(View.GONE);

        // Bring back the drafting container.
        mDraftContainerDrafting.setVisibility(View.VISIBLE);
    }
}
