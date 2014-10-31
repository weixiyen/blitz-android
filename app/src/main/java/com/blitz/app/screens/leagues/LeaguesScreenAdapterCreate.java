package com.blitz.app.screens.leagues;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blitz.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    private List<String>  mRecruitingLeagueNames = new ArrayList<String>();
    private List<Integer> mRecruitingLeagueRatings = new ArrayList<Integer>();
    private List<Integer> mRecruitingLeagueMemberCounts = new ArrayList<Integer>();

    private Callbacks mCallbacks;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Set callbacks.
     *
     * @param callbacks Callbacks.
     */
    public LeaguesScreenAdapterCreate(Callbacks callbacks) {

        // Set callbacks.
        mCallbacks = callbacks;
    }

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
                return configureRecruitingLeague(position - 2, view, parent);
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
        mRecruitingLeagueNames = leagueNames;
        mRecruitingLeagueRatings = leagueRatings;
        mRecruitingLeagueMemberCounts = leagueMemberCounts;

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

        // Wire the create button.
        convertView.findViewById(R.id.leagues_league_create)
                .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mCallbacks != null) {
                    mCallbacks.onCreateLeagueClicked();
                }
            }
        });

        // Wire the join button.
        convertView.findViewById(R.id.leagues_league_join)
                .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mCallbacks != null) {
                    mCallbacks.onJoinLeagueManualClicked();
                }
            }
        });

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

    /**
     * Configure a recruiting league view.
     *
     * @param position Position index, offset it by the static headers.
     * @param convertView Recycled view.
     * @param parent Parent.
     *
     * @return Configured recruiting league.
     */
    private View configureRecruitingLeague(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leagues_screen_league, parent, false);

            // Create and set associated view holder.
            convertView.setTag(new ViewHolder(convertView));
        }

        // Fetch view holder.
        ViewHolder viewHolder = (ViewHolder)convertView.getTag();

        int memberCount = mRecruitingLeagueMemberCounts.get(position);

        // Set the text.
        viewHolder.mLeagueName.setText(mRecruitingLeagueNames.get(position));
        viewHolder.mLeagueRating.setText(mRecruitingLeagueRatings.get(position) + " Rating");
        viewHolder.mLeagueMemberCount.setText(memberCount + (memberCount > 1 ? "Members" : "Member"));

        // Set join listener.
        viewHolder.mLeagueJoin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mCallbacks != null) {
                    mCallbacks.onJoinLeagueClicked(mRecruitingLeagueIds.get(position));
                }
            }
        });

        return convertView;
    }

    // endregion

    // region Inner Classes
    // ============================================================================================================

    static class ViewHolder {

        @InjectView(R.id.leagues_league_name)         TextView mLeagueName;
        @InjectView(R.id.leagues_league_rating)       TextView mLeagueRating;
        @InjectView(R.id.leagues_league_member_count) TextView mLeagueMemberCount;
        @InjectView(R.id.leagues_league_join)         TextView mLeagueJoin;

        private ViewHolder(View view) {

            // Map member variables.
            ButterKnife.inject(this, view);
        }
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks {

        public void onJoinLeagueClicked(String leagueId);
        public void onJoinLeagueManualClicked();

        public void onCreateLeagueClicked();
    }

    // endregion
}