package com.blitz.app.screens.stats;

import android.os.Bundle;
import android.widget.ListView;

import com.blitz.app.R;
import com.blitz.app.screens.main.PlayerListAdapter;
import com.blitz.app.screens.main.StatAdapter;
import com.blitz.app.utilities.android.BaseActivity;

import butterknife.InjectView;

/**
 * Created by Nate on 9/20/14.
 */
public class PlayerWeekStatsScreen extends BaseActivity {

    @InjectView(R.id.player_stats_list) ListView mStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mStats.setAdapter(new StatAdapter(getApplicationContext(), this,
                getIntent().getExtras().getStringArray(PlayerListAdapter.STAT_NAMES),
                getIntent().getExtras().getFloatArray(PlayerListAdapter.STAT_VALUES),
                getIntent().getExtras().getFloatArray(PlayerListAdapter.STAT_POINTS)));
        ;
    }
}
