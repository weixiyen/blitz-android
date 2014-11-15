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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Miguel on 11/10/2014. Copyright 2014 Blitz Studios
 */
public class BlitzDropdown extends ArrayAdapter<String> {

    // region Member Variables
    // ============================================================================================================

    // Associated views.
    @InjectView(R.id.blitz_dropdown_container)  View mBlitzDropdownContainer;
    @InjectView(R.id.blitz_dropdown_header) TextView mBlitzDropdownHeader;
    @InjectView(R.id.blitz_dropdown_list)   ListView mBlitzDropdownList;
    @InjectView(R.id.blitz_dropdown_list_wrap)  View mBlitzDropdownListWrap;

    // Selected position.
    private int mSelectedPosition;

    // Handle to the callbacks.
    private Callbacks mCallbacks;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Initialize dropdown.
     *
     * @param context Context needed for adapter.
     * @param rootView Root view that contains the dropdown UI.
     * @param dropdownNames List of names.
     */
    public BlitzDropdown(Context context, View rootView, List<String> dropdownNames) {
        super(context, 0, dropdownNames);

        ButterKnife.inject(this, rootView);

        // Configure the views.
        mBlitzDropdownList.setAdapter(this);
        mBlitzDropdownListWrap.setVisibility(View.GONE);
        mBlitzDropdownContainer.setOnClickListener(view -> {

            if (mBlitzDropdownList != null) {

                int visibility = mBlitzDropdownListWrap.getVisibility()
                        == View.VISIBLE ? View.GONE : View.VISIBLE;

                // Toggle the visibility of the list view.
                AnimHelperFade.setVisibility(mBlitzDropdownListWrap, visibility,
                        () -> mBlitzDropdownList.setSelection(mSelectedPosition));
            }
        });
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Inflate and configure the dropdown view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.blitz_dropdown_item, parent, false);
        }

        TextView view = ((TextView)convertView);

        // Configure view properties.
        view.setText(getItem(position));
        view.setTag(position);
        view.setTextColor(getContext().getResources().getColor(R.color.text_color_light));
        view.setOnClickListener(clickedView -> {

            int clickedPosition = (int)clickedView.getTag();

            if (clickedPosition != mSelectedPosition) {

                if (mBlitzDropdownContainer != null) {
                    mBlitzDropdownContainer.performClick();
                }

                if (mCallbacks != null) {
                    mCallbacks.onDropdownItemSelected((int)clickedView.getTag());
                }
            }
        });

        if (mSelectedPosition == position) {

            // Set the selected text link color if this is the selected view.
            view.setTextColor(getContext().getResources().getColor(R.color.selector_text_link));
        }

        return convertView;
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Set the dropdown callbacks.
     *
     * @param callbacks Callbacks
     */
    @SuppressWarnings("unused")
    public void setCallbacks(Callbacks callbacks) {

        // Set the callbacks.
        mCallbacks = callbacks;
    }

    /**
     * Update selected position.
     *
     * @param selectedPosition Selected position.
     */
    @SuppressWarnings("unused")
    public void setSelected(int selectedPosition) {

        // Update selected position.
        mSelectedPosition = selectedPosition;

        if (mBlitzDropdownHeader != null) {
            mBlitzDropdownHeader.setText(getItem(mSelectedPosition));
        }

        // Update the list.
        notifyDataSetChanged();
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks {

        public void onDropdownItemSelected(int position);
    }

    // endregion
}