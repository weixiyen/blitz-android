package com.blitz.app.base.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.utilities.reflection.ReflectionHelper;
import com.blitz.app.utilities.string.StringHelper;

import butterknife.ButterKnife;

/**
 * Created by Miguel Gaeta on 6/4/14.
 */
public class BaseDialogDeprecated extends DialogFragment {

    @SuppressWarnings("unused")
    private Integer mLayoutResID;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Auto inflate layout on creation and also
     * initialize butter knife.
     *
     * @param savedInstanceState Saved instance state.
     *
     * @return Initialized dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        onCreateView(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Fetch the class name string in the format of resource view.
        String underscoredClassName = StringHelper.camelCaseToLowerCaseUnderscores
                (((Object) this).getClass().getSimpleName());

        // Use reflection to fetch the associated view resource id and set content view.
        int layoutResourceId = ReflectionHelper.getResourceId(underscoredClassName, R.layout.class);

        if (mLayoutResID != null) {

            layoutResourceId = mLayoutResID;
        }

        @SuppressLint("InflateParams")
        View view = inflater.inflate(layoutResourceId, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);

        ButterKnife.inject(this, view);

        return builder.create();
    }

    /**
     * Reset butter knife on destroy view.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.reset(this);
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    @SuppressWarnings("unused")
    public void setContentView(int layoutResID) {
        mLayoutResID = layoutResID;
    }

    @SuppressWarnings("unused")
    public void onCreateView(Bundle savedInstanceState) {

    }
}