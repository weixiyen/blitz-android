package com.blitz.app.screens.recent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;

import java.util.List;

/**
 * Created by Miguel on 10/13/2014. Copyright 2014 Blitz Studios
 */
public class RecentScreenDropdownAdapter extends ArrayAdapter<String> {

    // region Constructor
    // =============================================================================================

    /**
     * Default constructor for a custom dropdown spinner.
     *
     * @param context Context.
     * @param objects Data source.
     */
    public RecentScreenDropdownAdapter(Context context, List<String> objects) {

        // Initialize with a random layout id, we will
        // be providing our own custom view.
        super(context, Integer.MIN_VALUE, objects);
    }

    // endregion

    /**
     * Fetch the dropdown header view.
     *
     * @param position Current selected item.
     * @param convertView Re-usable view.
     * @param parent Parent view group.
     *
     * @return Inflated and configured view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.recent_screen_dropdown_header, parent, false);
        }

        // Fetch the dropdown header.
        TextView dropdownHeader = (TextView)convertView
                .findViewById(R.id.recent_dropdown_header);

        // Set the selected dropdown item.
        dropdownHeader.setText(getItem(position));

        return convertView;
    }

    /**
     * Fetch the dropdown content view.
     *
     * @param position Current selected item.
     * @param convertView Re-usable view.
     * @param parent Parent view group.
     *
     * @return Inflated and configured view.
     */
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.recent_screen_dropdown_item, parent, false);
        }

        // Fetch the dropdown item.
        TextView dropdownItem = (TextView)convertView
                .findViewById(R.id.recent_dropdown_item);

        // Set the associated dropdown text.
        dropdownItem.setText(getItem(position));

        return convertView;
    }
}