package com.blitz.app.screens.stats;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.screens.main.PlayerListAdapter;
import com.blitz.app.screens.main.StatAdapter;
import com.blitz.app.utilities.android.BaseActivity;

import butterknife.InjectView;

/**
 * Created by Nate on 9/20/14.
 */
public class PlayerWeekStatsScreen extends BaseActivity {

    public static final String FIRST_NAME = "PlayerWeekStatsScreen.firstName";
    public static final String LAST_NAME = "PlayerWeekStatsScreen.lastName";
    public static final String TOTAL_POINTS = "PlayerWeekStatsScreen.totalPoints";
    public static final String WEEK = "PlayerWeekStatsScreen.week";

    @InjectView(R.id.player_stats_list) ListView mStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ((TextView)findViewById(R.id.first_name)).setText(getIntent().getStringExtra(FIRST_NAME));
        ((TextView)findViewById(R.id.last_name)).setText(getIntent().getStringExtra(LAST_NAME));
        ((TextView)findViewById(R.id.week)).setText(
                Integer.toString(getIntent().getExtras().getInt(WEEK)));
        ((TextView)findViewById(R.id.total_points)).setText(
                String.format("%.2f", getIntent().getExtras().getFloat(TOTAL_POINTS)));


        mStats.setAdapter(new StatAdapter(getApplicationContext(), this,
                getIntent().getExtras().getStringArray(PlayerListAdapter.STAT_NAMES),
                getIntent().getExtras().getFloatArray(PlayerListAdapter.STAT_VALUES),
                getIntent().getExtras().getFloatArray(PlayerListAdapter.STAT_POINTS)));
    }
}
