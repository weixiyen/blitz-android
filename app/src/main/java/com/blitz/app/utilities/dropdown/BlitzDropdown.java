package com.blitz.app.utilities.dropdown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.animations.AnimHelperFade;

import java.util.List;

/**
 * Created by Miguel on 11/10/2014. Copyright 2014 Blitz Studios
 */
public class BlitzDropdown extends ArrayAdapter<String> {

    private TextView mHeaderView;
    private ListView mListView;
    private View mContainerView;

    private List<String> mDropdownIds;
    private List<String> mDropdownNames;

    private int mSelectedPosition;

    public BlitzDropdown(Context context, List<String> dropdownIds, List<String> dropdownNames) {
        super(context, 0, dropdownIds);

        mDropdownIds = dropdownIds;
        mDropdownNames = dropdownNames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.blitz_dropdown_item, parent, false);
        }

        TextView view = ((TextView)convertView);

        // Configure view properties.
        view.setText(mDropdownNames.get(position));
        view.setTag(mDropdownIds.get(position));
        view.setTextColor(getContext().getResources().getColor(R.color.text_color_light));

        if (mSelectedPosition == position) {

            // Set the selected text link color if this is the selected view.
            view.setTextColor(getContext().getResources().getColor(R.color.selector_text_link));
        }

        return convertView;
    }

    // region Public Methods
    // ============================================================================================================

    /**
     * Update selected position.
     *
     * @param selectedPosition Selected position.
     */
    @SuppressWarnings("unused")
    public void setSelected(int selectedPosition) {

        // Update selected position.
        mSelectedPosition = selectedPosition;

        if (mHeaderView != null) {
            mHeaderView.setText(mDropdownNames.get(mSelectedPosition));
        }

        // Update the list.
        notifyDataSetChanged();
    }

    /**
     * Set the associated list view.
     *
     * @param listView List view.
     */
    @SuppressWarnings("unused")
    public void setListView(ListView listView) {

        if (listView == null) {

            return;
        }

        // Set the list view.
        mListView = listView;
        mListView.setVisibility(View.GONE);
        mListView.setAdapter(this);
    }

    /**
     * Set associated header view.
     *
     * @param headerView Header view.
     */
    @SuppressWarnings("unused")
    public void setHeaderView(TextView headerView) {

        // Set the headerView.
        mHeaderView = headerView;
    }

    /**
     * Set the container view.
     *
     * @param containerView Container view.
     */
    @SuppressWarnings("unused")
    public void setContainerView(View containerView) {

        if (containerView == null) {

            return;
        }

        mContainerView = containerView;
        mContainerView.setOnClickListener(view -> {

            if (mListView != null) {

                // Toggle the visibility of the list view.
                AnimHelperFade.setVisibility(mListView, mListView.getVisibility()
                        == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }

    // endregion
}
