package com.blitz.app.screens.main;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.blitz.app.R;
import com.blitz.app.base.activity.BaseActivity;

import butterknife.OnClick;

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


    }
}