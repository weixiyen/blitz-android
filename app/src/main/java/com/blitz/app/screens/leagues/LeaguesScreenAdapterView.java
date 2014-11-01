package com.blitz.app.screens.leagues;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blitz.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrkcsc on 10/26/14. Copyright 2014 Blitz Studios
 */
public class LeaguesScreenAdapterView extends BaseAdapter {

    // region Member Variables
    // ============================================================================================================

    public static final int LEAGUE_STATS_HEADER = 0;
    public static final int LEAGUE_MEMBER       = 1;
    public static final int LEAGUE_LOADING      = 2;

    private int leagueRank;
    private int leagueRating;
    private int leagueMembers;

    private List<String>  memberUserIds   = new ArrayList<String>();
    private List<String>  memberUserNames = new ArrayList<String>();
    private List<Integer> memberWins      = new ArrayList<Integer>();
    private List<Integer> memberLosses    = new ArrayList<Integer>();
    private List<Integer> memberRatin     = new ArrayList<Integer>();

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    @Override
    public int getCount() {

        // Either show stats + members, or just the loading section.
        return memberUserIds.size() > 0 ? 1 + memberUserIds.size() : 1;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        switch (getItemViewType(position)) {

            case LEAGUE_STATS_HEADER:

                // Configure item with calls to action.
                return configureLeagueLoading(view, parent);

            case LEAGUE_MEMBER:

                // Configure item of header for recruiting leagues.
                return configureLeagueLoading(view, parent);

            case LEAGUE_LOADING:

                // Configure the loading view item.
                return configureLeagueLoading(view, parent);
        }

        return view;
    }

    /**
     * The first position is a static list item,
     * anything after is a league item.
     *
     * @param position Position.
     *
     * @return Item view type.
     */
    @Override
    public int getItemViewType(int position) {

        switch (position) {

            case 0:
                return LEAGUE_STATS_HEADER;

            default:

                // Show loading section if no league members.
                return memberUserIds.size() > 0 ? LEAGUE_MEMBER : LEAGUE_LOADING;
        }
    }

    /**
     * Two view types.
     *
     * @return View type count.
     */
    @Override
    public int getViewTypeCount() {

        return 3;
    }

    // endregion

    // region Private Members
    // ============================================================================================================

    /**
     * Configure item for loading view.
     *
     * @param convertView Recycled view.
     * @param parent Parent.
     *
     * @return Configured loading view.
     */
    private View configureLeagueLoading(View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leagues_screen_loading, parent, false);
        }

        // Ensure visibility.
        convertView.setVisibility(View.VISIBLE);

        return convertView;
    }

    // endregion
}