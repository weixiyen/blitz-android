package com.blitz.app.dialogs;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseDialogFragment;
import com.blitz.app.utilities.logging.LogHelper;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by mrkcsc on 8/17/14.
 */
public class DialogRules extends BaseDialogFragment {

    @InjectView(R.id.dialog_rules_pager) ViewPager mRulesViewPager;

    @Override
    protected void onViewCreated(View view) {

        //mRulesViewPager = (ViewPager)view.findViewById(R.id.dialog_rules_pager);

        LogHelper.log("View pager: " + mRulesViewPager);
    }

    private List getFragments(){
        //List fList = new ArrayList();
        //fList.add(FragmentAcoesMusculares.newInstance("Fragment 1",1));
        //fList.add(FragmentAcoesMusculares.newInstance("Fragment 2",2));
        //fList.add(FragmentAcoesMusculares.newInstance("Fragment 3",3));
        return null;
    }
}