package com.blitz.app.dialogs.rules;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blitz.app.R;

/**
 * Fragment for rules. This is the fragment for the individual rules cards, though it could probably
 * be generic for any kind of content fragment.
 *
 * Created by Nate on 10/5/14.
 */
public class DialogRulesAdapterFragment extends Fragment {

    private String mContent;

    public static DialogRulesAdapterFragment newInstance() {

        return new DialogRulesAdapterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.dialog_rules_adapter_fragment, container, false);

        TextView webView = (TextView) view.findViewById(R.id.dialog_rules_adapter_fragment_content);

        webView.setText(Html.fromHtml(mContent));

        return view;
    }

    /**
     * Set content to display in this fragment.
     * @param content html content.
     */
    void setContent(String content) {

        mContent = content;
    }


}
