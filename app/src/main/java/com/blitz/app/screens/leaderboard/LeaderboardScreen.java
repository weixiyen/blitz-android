package com.blitz.app.screens.leaderboard;

import android.widget.ListView;

import com.blitz.app.R;
import com.blitz.app.object_models.RestModelUser;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.reactive.Observer;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelLeaderboard;

import java.util.List;

import butterknife.InjectView;

/**
 * Screen that shows players with the highest Elo rating.
 *
 * Created by Nate on 9/29/14.
 */
public class LeaderboardScreen extends BaseActivity implements Observer<List<RestModelUser>> {

    @InjectView(R.id.leaderboard_list) ListView mPlayerList;

    private ViewModelLeaderboard mViewModel;

    @Override
    public ViewModel onFetchViewModel() {

        if(mViewModel == null) {
            mViewModel = new ViewModelLeaderboard(this, this);
        }

        return mViewModel;
    }

    @Override public void onNext(List<RestModelUser> leaders) {

        if (mPlayerList != null) {
            mPlayerList.setAdapter(new LeaderboardListAdapter(this, leaders));
        }

    }



}
