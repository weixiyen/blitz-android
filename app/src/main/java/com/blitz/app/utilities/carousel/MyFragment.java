package com.blitz.app.utilities.carousel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blitz.app.R;

/**
 * Created by mrkcsc on 8/17/14.
 */
public class MyFragment extends Fragment {

    public static Fragment newInstance(Context context, int pos, float scale)
    {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        return Fragment.instantiate(context, MyFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        MyLinearLayout l = (MyLinearLayout)
                inflater.inflate(R.layout.blitz_carousel_helmet, container, false);

        int pos = this.getArguments().getInt("pos");


        float scale = this.getArguments().getFloat("scale");

        l.setScaleBoth(scale);

        return l;
    }
}