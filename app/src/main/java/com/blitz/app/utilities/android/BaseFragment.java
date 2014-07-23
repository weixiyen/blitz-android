package com.blitz.app.utilities.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blitz.app.R;
import com.blitz.app.models.comet.CometAPIManager;
import com.blitz.app.utilities.reflection.ReflectionHelper;
import com.blitz.app.utilities.string.StringHelper;

import butterknife.ButterKnife;

/**
 * Shared base functionality across all fragments.
 */
public class BaseFragment extends Fragment {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    private View mContentView;
    private ViewGroup mContainer;
    private LayoutInflater mInflater;

    //==============================================================================================
    // Overwritten Methods
    //==============================================================================================

    /**
     * Saves some parameters in order to expose the activity
     * style "setContentView" method into fragment children.
     *
     * @param inflater Layout inflater.
     * @param container Parent container.
     * @param savedInstanceState Saved state.
     *
     * @return Inflated view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Save container.
        mContainer = container;
        mInflater = inflater;

        // Fetch the class name string in the format of resource view.
        String underscoredClassName = StringHelper.camelCaseToLowerCaseUnderscores
                (((Object) this).getClass().getSimpleName());

        // Use reflection to fetch the associated view resource id and set content view.
        setContentView(ReflectionHelper.getResourceId(underscoredClassName, R.layout.class));

        // Call simpler version of create view.
        onCreateView(savedInstanceState);

        return mContentView;
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
     * Returns normal root if exists or
     * the content view otherwise.
     *
     * @return Root view.
     */
    @Override
    public View getView() {
        View view = super.getView();

        if (view == null) {
            view = mContentView;
        }

        return view;
    }

    /**
     * Keep track of fragments.
     */
    @Override
    public void onResume () {
        super.onResume();

        // Add a new fragment.
        CometAPIManager.configAddFragment(this);
    }

    /**
     * Keep track of fragments.
     */
    @Override
    public void onPause () {
        super.onPause();

        // Remove fragment.
        CometAPIManager.configRemoveFragment(this);
    }

    //==============================================================================================
    // Protected Methods
    //==============================================================================================

    /**
     * @see android.app.Fragment#startActivity(android.content.Intent)
     *
     * Call the activities version of this method.  This is done to
     * ensure that the proper custom transitions get applied.
     */
    public void startActivity (Intent intent) {
        getActivity().startActivity(intent);
    }

    /**
     * A simpler version of on create view.  Can be used
     * as a convenience method which abstracts away
     * some of the details of a fragment and makes them
     * more like activities.
     *
     * @param savedInstanceState The saved instance state.
     */
    @SuppressWarnings("unused")
    protected void onCreateView(Bundle savedInstanceState) {

    }

    /**
     * Exposes activity set content view syntax into fragments.
     *
     * @param layoutResID Layout resource.
     */
    protected void setContentView (int layoutResID) {

        // Inflate and store the content view.
        mContentView = mInflater.inflate(layoutResID, mContainer, false);

        // Inject butter-knife views.
        ButterKnife.inject(this, mContentView);
    }
}