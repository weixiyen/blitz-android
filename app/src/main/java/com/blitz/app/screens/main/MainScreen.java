package com.blitz.app.screens.main;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.models.objects.ObjectModelPreferences;
import com.blitz.app.models.rest.RestAPIOperation;

import butterknife.OnClick;

public class MainScreen extends BaseActivity implements ActionBar.TabListener {

    private ObjectModelPreferences mModelPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getActionBar();

        if (bar != null) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            for (int i=1; i <= 3; i++) {
                ActionBar.Tab tab = bar.newTab();
                tab.setText("Tab " + i);
                tab.setTabListener(this);
                bar.addTab(tab);
            }
        }
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.main_screen_play) @SuppressWarnings("unused")
    public void main_screen_play() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        if (mModelPreferences == null) {
            mModelPreferences = new ObjectModelPreferences();
        }

        // Set desired registration fields.
        mModelPreferences.TestCall(new RestAPIOperation(this) {

            @Override
            public void success() {

                Log.e("Blitz", "Test");
            }
        });
    }
}