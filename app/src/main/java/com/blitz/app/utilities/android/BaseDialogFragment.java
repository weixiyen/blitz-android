package com.blitz.app.utilities.android;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.blitz.app.R;
import com.blitz.app.utilities.reflection.ReflectionHelper;

import butterknife.ButterKnife;

/**
 * Created by mrkcsc on 8/17/14. Copyright 2014 Blitz Studios
 */
public abstract class BaseDialogFragment extends DialogFragment {

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * When view is created inflate layout and setup the window.
     *
     * @param inflater Inflater object.
     * @param container Parent container.
     * @param savedInstanceState Saved state.
     *
     * @return Instantiated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Use reflection to fetch the associated view resource id and set content view.
        int layoutResourceId = ReflectionHelper.getResourceId(this.getClass(), R.layout.class);

        // Now inflate the associated view.
        View view = inflater.inflate(layoutResourceId, container);

        // Fetch dialog window.
        Window window = getDialog().getWindow();

        // No title mode (or status bar).
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Initialize butter-knife.
        ButterKnife.inject(this, view);

        // Call on view created.
        onViewCreated(view);

        return view;
    }

    /**
     * Destroy butter-knife injections.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Reset butter-knife.
        ButterKnife.reset(this);
    }

    /**
     * When activity is created, make this dialog
     * transparent as possible and full screen.
     *
     * @param savedInstanceState Saved state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Fetch dialog window.
        Window window = getDialog().getWindow();

        // Make the background transparent.
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Make the dialog full screen.
        window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
    }

    // endregion

    // region Abstract Methods
    // ============================================================================================================

    protected abstract void onViewCreated(View view);

    // endregion
}