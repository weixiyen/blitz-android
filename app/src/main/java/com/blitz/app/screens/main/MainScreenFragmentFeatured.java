package com.blitz.app.screens.main;

import com.blitz.app.R;
import com.blitz.app.models.objects.ObjectModelPlay;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.utilities.android.BaseFragment;
import com.blitz.app.utilities.logging.LogHelper;

import butterknife.OnClick;

/**
 * Created by mrkcsc on 7/14/14.
 */
public class MainScreenFragmentFeatured extends BaseFragment {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    private ObjectModelPlay mModelPlay;

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.main_featured_play) @SuppressWarnings("unused")
    public void main_screen_play() {

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

                LogHelper.log("In queue! - Show the dialog!");
            }
        });
    }
}