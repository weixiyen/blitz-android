package com.blitz.app.screens.leagues;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blitz.app.R;
import com.blitz.app.utilities.logging.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrkcsc on 10/26/14. Copyright 2014 Blitz Studios
 */
public class LeaguesScreenAdapterCreate extends BaseAdapter {

    // region Member Variables
    // ============================================================================================================

    public static final int CREATE_JOIN_HEADER        = 0;
    public static final int RECRUITING_LEAGUE_HEADER  = 1;
    public static final int RECRUITING_LEAGUE         = 2;
    public static final int RECRUITING_LEAGUE_LOADING = 3;

    // List of recruiting leagues.
    private List<String>  mRecruitingLeagueIds = new ArrayList<String>();
    private List<String>  mRecruitingleagueNames = new ArrayList<String>();
    private List<Integer> mRecruitingleagueRatings = new ArrayList<Integer>();
    private List<Integer> mRecruitingleagueMemberCounts = new ArrayList<Integer>();

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Fetch total number of items.
     *
     * @return Total items.
     */
    @Override
    public int getCount() {

        LogHelper.log("Size: " + mRecruitingLeagueIds.size());

        // Top two sections are static, rest is either a loading view
        // or the actual leagues that are recruiting.
        return mRecruitingLeagueIds.size() > 0 ? 2 + mRecruitingLeagueIds.size() : 3;
    }

    /**
     * No need for this method.
     */
    @Override
    public Object getItem(int position) {

        return null;
    }

    /**
     * Unique item identifier.
     *
     * @param position Position.
     *
     * @return Item identifier.
     */
    @Override
    public long getItemId(int position) {

        return position;
    }

    /**
     * Fetch view as specified position.
     *
     * @param position Position.
     * @param view Recycled view.
     * @param parent Parent.
     *
     * @return Configured view.
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        switch (getItemViewType(position)) {

            case CREATE_JOIN_HEADER:

                // Configure item with calls to action.
                return configureHeader(view, parent);

            case RECRUITING_LEAGUE_HEADER:

                // Configure item of header for recruiting leagues.
                return configureRecruitingLeagueHeader(view, parent);

            case RECRUITING_LEAGUE_LOADING:

                // Configure the loading view item.
                return configureRecruitingLeagueLoading(view, parent);

            case RECRUITING_LEAGUE:

                // Configure item that represents a recruiting league.
                return configureRecruitingLeague(position, view, parent);
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
                return CREATE_JOIN_HEADER;

            case 1:
                return RECRUITING_LEAGUE_HEADER;

            case 2:

                // Show loading section if no leagues.
                return mRecruitingLeagueIds.size() > 0
                        ? RECRUITING_LEAGUE : RECRUITING_LEAGUE_LOADING;

            default:
                return RECRUITING_LEAGUE;
        }
    }

    /**
     * Two view types.
     *
     * @return View type count.
     */
    @Override
    public int getViewTypeCount() {

        return 4;
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Set the recruiting leagues.
     *
     * @param leagueIds League ids.
     * @param leagueNames League names.
     * @param leagueRatings League ratings.
     * @param leagueMemberCounts League member counts.
     */
    @SuppressWarnings("unused")
    public void setRecruitingLeagues(List<String> leagueIds,
                                     List<String> leagueNames,
                                     List<Integer> leagueRatings,
                                     List<Integer> leagueMemberCounts) {

        mRecruitingLeagueIds = leagueIds;
        mRecruitingleagueNames = leagueNames;
        mRecruitingleagueRatings = leagueRatings;
        mRecruitingleagueMemberCounts = leagueMemberCounts;

        // Reload table.
        notifyDataSetChanged();
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Configure the header for create league
     * and join a league.
     *
     * @param convertView Recycled view.
     * @param parent Parent.
     *
     * @return Configured header.
     */
    private View configureHeader(View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leagues_screen_create_join, parent, false);
        }

        return convertView;
    }

    /**
     * Configure header for the recruiting leagues.
     *
     * @param convertView Recycled view.
     * @param parent Parent.
     *
     * @return Configured header.
     */
    private View configureRecruitingLeagueHeader(View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leagues_screen_recruiting, parent, false);
        }

        return convertView;
    }

    /**
     * Configure item for loading view.
     *
     * @param convertView Recycled view.
     * @param parent Parent.
     *
     * @return Configured loading view.
     */
    private View configureRecruitingLeagueLoading(View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leagues_screen_loading, parent, false);
        }

        return convertView;
    }

    private View configureRecruitingLeague(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leagues_screen_league, parent, false);

            // Create and set associated view holder.
            //view.setTag(new LeaderboardListItemViewHolder(convertView));
        }

        // Fetch view holder.
        //LeaderboardListItemViewHolder viewHolder =
        //        (LeaderboardListItemViewHolder)convertView.getTag();

        // Set the text.
        //viewHolder.mLeaderboardIndex.setText(String.format("%03d", position + 1));
        //viewHolder.mLeaderboardUserName.setText(mUserNames.get(position));
        //viewHolder.mLeaderboardWins.setText("W" + String.format("%03d", mUserWins.get(position)));
        //viewHolder.mLeaderboardLosses.setText("L" + String.format("%03d", mUserLosses.get(position)));
        //viewHolder.mLeaderboardRating.setText(Integer.toString(mUserRating.get(position)));

        // Set the image.
        //viewHolder.mLeaderboardHelmet.setImageBitmap(null);
        //viewHolder.mLeaderboardHelmet.setImageUrl(mUserAvatarUrls.get(position));

        return convertView;
    }

    // endregion
}