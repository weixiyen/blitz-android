package com.blitz.app.screens.leagues;

import android.os.Bundle;
import android.widget.ListView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseFragment;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 10/24/14. Copyright 2014 Blitz Studios
 */
public class LeaguesScreen extends BaseFragment {

    @InjectView(R.id.leagues_screen_list) ListView leaguesScreenList;

    /**
     * A simpler version of on create view.  Can be used
     * as a convenience method which abstracts away
     * some of the details of a fragment and makes them
     * more like activities.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);

        leaguesScreenList.setAdapter(new LeaguesScreenAdapterCreate());
    }
}