package com.blitz.app.screens.leagues;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.blitz.app.R;
import com.blitz.app.utilities.animations.AnimHelperFade;

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

    private int mLeagueRank;
    private int mLeagueRating;
    private int mLeagueMembers;

    private List<String>  mMemberUserIds   = new ArrayList<String>();
    private List<String>  mMemberUserNames = new ArrayList<String>();
    private List<Integer> mMemberWins      = new ArrayList<Integer>();
    private List<Integer> mMemberLosses    = new ArrayList<Integer>();
    private List<Integer> mMemberRatin     = new ArrayList<Integer>();

    // List view paired to this adapter.
    private ListView mAssociatedListView;

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    @Override
    public int getCount() {

        // Either show stats + members, or just the loading section.
        return mMemberUserIds.size() > 0 ? 1 + mMemberUserIds.size() : 1;
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
                return mMemberUserIds.size() > 0 ? LEAGUE_MEMBER : LEAGUE_LOADING;
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

    // region Public Methods
    // ============================================================================================================

    /**
     * Set associated list view.
     *
     * @param listView Associated list view.
     */
    @SuppressWarnings("unused")
    public void setAssociatedListView(ListView listView) {

        mAssociatedListView = listView;
    }

    /**
     * Set league information.
     *
     * @param leagueRank Rank.
     * @param leagueRating Rating.
     * @param leagueMembers Members.
     * @param memberUserIds User ids.
     * @param memberUserNames User names.
     * @param memberWins User wins.
     * @param memberLosses User losses.
     * @param memberRating User rating.
     */
    @SuppressWarnings("unused")
    public void setLeagueInfo(final int leagueRank, final int leagueRating, final int leagueMembers,
                              final List<String>  memberUserIds,
                              final List<String>  memberUserNames,
                              final List<Integer> memberWins,
                              final List<Integer> memberLosses,
                              final List<Integer> memberRating) {

        View loadingView = null;

        // Ensure loading view exists.
        if (getItemViewType(LEAGUE_LOADING - 1) == LEAGUE_LOADING) {

            // Fetch it.
            loadingView = mAssociatedListView.getChildAt(LEAGUE_LOADING - 1);
        }

        // Update functionality.
        Runnable updateAdapter = new Runnable() {

            @Override
            public void run() {

                mLeagueRank    = leagueRank;
                mLeagueRating  = leagueRating;
                mLeagueMembers = leagueMembers;

                mMemberUserIds   = memberUserIds;
                mMemberUserNames = memberUserNames;
                mMemberWins      = memberWins;
                mMemberLosses    = memberLosses;
                mMemberRatin     = memberRating;

                // Reload table.
                notifyDataSetChanged();
            }
        };

        // If no loading view.
        if (loadingView == null) {

            // Update immediately.
            updateAdapter.run();
        } else {

            // First fade the loading spinner before updating.
            AnimHelperFade.setVisibility(loadingView.findViewById(R.id.leagues_loading),
                    View.INVISIBLE, updateAdapter);
        }
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