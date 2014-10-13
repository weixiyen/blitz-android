package com.blitz.app.utilities.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blitz.app.R;
import com.blitz.app.utilities.animations.AnimHelper;
import com.blitz.app.utilities.blitz.BlitzDelay;
import com.blitz.app.utilities.comet.CometAPIManager;
import com.blitz.app.utilities.reflection.ReflectionHelper;
import com.blitz.app.utilities.string.StringHelper;
import com.blitz.app.view_models.ViewModel;

import butterknife.ButterKnife;

/**
 * Shared base functionality across all fragments.
 */
public class BaseFragment extends Fragment {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Content view of the fragment.
    private View mContentView;

    // Parent container.
    private ViewGroup mContainer;

    // Layout inflater.
    private LayoutInflater mInflater;

    // Use to delay initialization of our view models which
    // alleviates perceived lag during transition animations.
    private Handler mViewModelInitializeHandler;

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

        // Restore view model state.
        if (getViewModel() != null) {
            getViewModel().restoreInstanceState(savedInstanceState);
        }

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
    public void onResume() {
        super.onResume();

        // Initialize the view model on the screen transition delay.
        mViewModelInitializeHandler = BlitzDelay.postDelayed(new Runnable() {

            @Override
            public void run() {

                // Initialize view model.
                if (getViewModel() != null) {
                    getViewModel().initialize();
                }
            }
        }, AnimHelper.getConfigAnimTimeStandard(this.getActivity()));

        // Add a new fragment.
        CometAPIManager.configAddFragment(this);
    }

    /**
     * Keep track of fragments.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Remove pending initialize calls.
        BlitzDelay.remove(mViewModelInitializeHandler);

        // Stop view model.
        if (getViewModel() != null) {
            getViewModel().stop();
        }

        // Remove fragment.
        CometAPIManager.configRemoveFragment(this);
    }

    /**
     * Save this screen fragments state.
     *
     * @param outState Outbound state bundle.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save view model state.
        if (getViewModel() != null) {
            getViewModel().saveInstanceState(outState);
        }
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Attempts to fetch the associated instance
     * of the view model for lifecycle callbacks.
     *
     * @return View model or null if not found.
     */
    private ViewModel getViewModel() {

        // If we implement view model callbacks.
        if (this instanceof ViewModel.ViewModelCallbacks) {

            // That means we can fetch the view model.
            return ((ViewModel.ViewModelCallbacks)this).onFetchViewModel();
        }

        return null;
    }

    /**
     * Exposes activity set content view syntax into fragments.
     *
     * @param layoutResId Layout resource.
     */
    private void setContentView(int layoutResId) {

        // Inflate and store the content view.
        mContentView = mInflater.inflate(layoutResId, mContainer, false);

        // Inject butter-knife views.
        ButterKnife.inject(this, mContentView);
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
    @SuppressWarnings("unused")
    public void startActivity(Intent intent) {
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
     * Fetch base activity.
     *
     * @return Base activity.
     */
    @SuppressWarnings("unused")
    public BaseActivity getBaseActivity() {

        return (BaseActivity)super.getActivity();
    }
}