package com.blitz.app.screens.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.blitz.app.R;

import java.util.Arrays;

/**
 * Created by Nate on 9/24/14.
 */
public class StatAdapter extends ArrayAdapter {

    final String[] mStatNames;
    final float[] mStatValues;
    final float[] mStatPoints;
    final Activity mActivity;

    public StatAdapter(Context context, Activity activity, String[] statNames, float[] statValues, float[] statPoints) {
        super(context, R.layout.player_week_stats_screen, Arrays.asList(1, 2, 3));

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

        return v;
    }
}
