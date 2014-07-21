package com.blitz.app.screens.main;

import android.view.View;

import com.blitz.app.R;
import com.blitz.app.models.objects.ObjectModelPlay;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.logging.LogHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class MainScreenFragmentFeatured extends BaseFragment {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Container views.
    @InjectView(R.id.main_featured_timeline_container) View mTimelineContainer;
    @InjectView(R.id.main_featured_queued_container)   View   mQueuedContainer;

    private ObjectModelPlay mModelPlay;

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.main_featured_play) @SuppressWarnings("unused")
    public void main_featured_play() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        if (mModelPlay == null) {
            mModelPlay = new ObjectModelPlay();
        }

        // Enter the queue.
        mModelPlay.enterQueue(getActivity(), new ObjectModelPlay.EnterQueueCallback() {

            @Override
            public void onEnterQueue() {

                // Show the queued container UI.
                mTimelineContainer.setVisibility(View.GONE);
                  mQueuedContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.main_featured_cancel) @SuppressWarnings("unused")
    public void main_featured_cancel() {

        LogHelper.log("TODO: Send the cancel call.");

        // Show the timeline container UI.
        mTimelineContainer.setVisibility(View.VISIBLE);
          mQueuedContainer.setVisibility(View.GONE);
    }
}