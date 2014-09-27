package com.blitz.app.screens.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;

/**
 * Created by Nate on 9/24/14.
 */
public class StatAdapter extends ArrayAdapter {

    final String[] mStatNames;
    final float[] mStatValues;
    final float[] mStatPoints;
    final Activity mActivity;

    public StatAdapter(Context context, Activity activity, String[] statNames, float[] statValues, float[] statPoints) {
        super(context, R.layout.stats_breakdown_screen, statNames);

        if(statNames.length != statValues.length || statNames.length != statPoints.length) {
            throw new IllegalArgumentException("Stat arrays must have the same length");
        }

        mActivity = activity;
        mStatNames = statNames;
        mStatValues = statValues;
        mStatPoints = statPoints;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(mActivity)
                    .inflate(R.layout.player_week_stats_list_item, null);
        }

        if(position < mStatValues.length) {

            final String statCountAndName = mStatValues[position] + " " + mStatNames[position];
            ((TextView) v.findViewById(R.id.stat_count_and_name)).setText(statCountAndName);
            ((TextView) v.findViewById(R.id.stat_points)).setText(String.format("%.2f", mStatPoints[position]));
        }

        return v;
    }
}
