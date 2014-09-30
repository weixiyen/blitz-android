package com.blitz.app.utilities.carousel;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blitz.app.R;

/**
 * Created by mrkcsc on 8/17/14. Copyright 2014 Blitz Studios
 */
public class BlitzCarouselAdapterFragment extends Fragment {

    public static Fragment newInstance(Context context, int pos, float scale)
    {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);
        return Fragment.instantiate(context, BlitzCarouselAdapterFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        BlitzCarouselScalingView l = (BlitzCarouselScalingView)
                inflater.inflate(R.layout.blitz_carousel_helmet, container, false);

        int pos = getArguments().getInt("pos");

        float scale = this.getArguments().getFloat("scale");

        l.setScale(scale);

        return l;
    }
}