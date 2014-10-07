package com.blitz.app.screens.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseFragment;

/**
 * Fragment for rules. This is the fragment for the individual rules cards, though it could probably
 * be generic for any kind of content fragment.
 *
 * Created by Nate on 10/5/14.
 */
public class RulesFragment extends BaseFragment {

    private String mContent;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.rules_fragment, container, false);
        TextView closeButton = (TextView) view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _) {
                container.setVisibility(View.GONE);
            }
        });
        WebView webView = (WebView) view.findViewById(R.id.content);
        webView.loadData(mContent, "text/html", "utf-8");
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