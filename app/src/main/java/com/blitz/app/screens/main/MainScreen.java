package com.blitz.app.screens.main;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.screens.sign_up.SignUpScreen;

public class MainScreen extends BaseActivity implements ActionBar.TabListener {

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

    /**
     * Start next activity in the flow.
     */
    public void transitionNextScreen() {

        // Create intent for main screen activity.
        Intent intent = new Intent(this, SignUpScreen.class);

        startActivity(intent);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}