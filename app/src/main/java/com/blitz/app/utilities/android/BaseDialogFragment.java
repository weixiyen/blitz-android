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

import java.util.List;

/**
 * Created by mrkcsc on 8/17/14.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected abstract void onViewCreated(View view);

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

        // Call on view created.
        onViewCreated(view);

        return view;
    }

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

    private List getFragments(){
        //List fList = new ArrayList();
        //fList.add(FragmentAcoesMusculares.newInstance("Fragment 1",1));
        //fList.add(FragmentAcoesMusculares.newInstance("Fragment 2",2));
        //fList.add(FragmentAcoesMusculares.newInstance("Fragment 3",3));
        return null;
    }
}