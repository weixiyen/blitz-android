package com.blitz.app.screens.leagues;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.blitz.app.R;
import com.blitz.app.utilities.animations.AnimHelperFade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by mrkcsc on 10/26/14. Copyright 2014 Blitz Studios
 */
public class LeaguesScreenAdapterView extends BaseAdapter {

    // region Member Variables
    // ============================================================================================================

    private static final int LEAGUE_STATS_HEADER      = 0;
    private static final int LEAGUE_RECRUITING_TOGGLE = 1;
    private static final int LEAGUE_MEMBER            = 2;
    private static final int LEAGUE_LOADING           = 3;

    private static List<Integer> mViewTypes =  Arrays.asList(
            LEAGUE_STATS_HEADER,
            LEAGUE_RECRUITING_TOGGLE,
            LEAGUE_MEMBER,
            LEAGUE_LOADING);

    private String mLeagueId;

    private int mLeagueRank;
    private int mLeagueRating;
    private int mLeagueMembers;

    private boolean mIsOfficer;
    private boolean mIsRecruiting;

    private List<String>  mMemberUserIds   = new ArrayList<String>();
    private List<String>  mMemberUserNames = new ArrayList<String>();
    private List<Integer> mMemberWins      = new ArrayList<Integer>();
    private List<Integer> mMemberLosses    = new ArrayList<Integer>();
    private List<Integer> mMemberRating    = new ArrayList<Integer>();

    // List view paired to this adapter.
    private ListView mAssociatedListView;

    // Callbacks object.
    private Callbacks mCallbacks;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Set callbacks.
     *
     * @param callbacks Callbacks.
     */
    public LeaguesScreenAdapterView(Callbacks callbacks) {

        // Set callbacks.
        mCallbacks = callbacks;
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Total count of items.
     *
     * @return Item count.
     */
    @Override
    public int getCount() {

        int nonMemberCells = mIsOfficer ? 2 : 1;

        // Either show stats + members, or just the loading section.
        return mMemberUserIds.size() > 0 ? nonMemberCells + mMemberUserIds.size() : 1;
    }

    /**
     * Unused.
     */
    @Override
    public Object getItem(int position) {

        return null;
    }

    /**
     * Position as id.
     */
    @Override
    public long getItemId(int position) {

        return position;
    }

    /**
     * Return and configured appropriate view for type.
     *
     * @param position Item position.
     * @param view View.
     * @param parent Parent.
     *
     * @return Inflated and configured view.
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        switch (getItemViewType(position)) {

            case LEAGUE_STATS_HEADER:

                // Configure item with calls to action.
                return configureStatsHeader(view, parent);

            case LEAGUE_RECRUITING_TOGGLE:

                // Configure toggle section.
                return configureRecruitingToggle(view, parent);

            case LEAGUE_LOADING:

                // Configure the loading view item.
                return configureLeagueLoading(view, parent);

            case LEAGUE_MEMBER:

                int offset = mIsOfficer ? 2 : 1;

                // Configure item of header for recruiting leagues.
                return configureLeagueMember(view, parent, position - offset);
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

        if (mMemberUserIds.size() > 0) {

            switch (position) {

                case 0:
                    return LEAGUE_STATS_HEADER;
                case 1:
                    return mIsOfficer ? LEAGUE_RECRUITING_TOGGLE : LEAGUE_MEMBER;
                default:
                    return LEAGUE_MEMBER;
            }
        } else {

            switch (position) {

                default:
                    return LEAGUE_LOADING;
            }
        }
    }

    /**
     * Two view types.
     *
     * @return View type count.
     */
    @Override
    public int getViewTypeCount() {

        return mViewTypes.size();
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
    public void setLeagueInfo(final String leagueId, final int leagueRank,
                              final int leagueRating, final int leagueMembers,
                              final boolean isOfficer, final boolean isRecruiting,
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

                mLeagueId      = leagueId;
                mLeagueRank    = leagueRank;
                mLeagueRating  = leagueRating;
                mLeagueMembers = leagueMembers;
                mIsOfficer     = isOfficer;
                mIsRecruiting  = isRecruiting;

                mMemberUserIds   = memberUserIds;
                mMemberUserNames = memberUserNames;
                mMemberWins      = memberWins;
                mMemberLosses    = memberLosses;
                mMemberRating = memberRating;

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
     * Configure the stats header.
     *
     * @param convertView Recycled view.
     * @param parent Parent.
     *
     * @return Configured item.
     */
    private View configureStatsHeader(View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leagues_screen_summary, parent, false);

            // Set the view holder.
            convertView.setTag(new ViewHolderHeader(convertView));
        }

        // Fetch view holder.
        ViewHolderHeader viewHolder = (ViewHolderHeader)convertView.getTag();

        viewHolder.mLeagueRank.setText(Integer.toString(mLeagueRank));
        viewHolder.mLeagueMembers.setText(Integer.toString(mLeagueMembers));
        viewHolder.mLeagueRating.setText(Integer.toString(mLeagueRating));

        return convertView;
    }

    /**
     * Configure the view that toggles open recruitment.
     *
     * @param convertView Recycled view.
     * @param parent Parent.
     *
     * @return Configured view.
     */
    private View configureRecruitingToggle(View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leagues_screen_recruitment_toggle, parent, false);
        }

        // Find the recruiting toggle view.
        Switch recruitingToggle = ((Switch)convertView.findViewById(R.id.leagues_recruitment_toggle));

        // Track state changes of the recruitment toggle.
        recruitingToggle.setChecked(mIsRecruiting);
        recruitingToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (mCallbacks != null) {
                    mCallbacks.onRecruiting(mLeagueId, checked);
                }
            }
        });

        return convertView;
    }

    /**
     * Configure a league member item.
     *
     * @param convertView Recycled view.
     * @param parent Parent.
     * @param position Position.
     *
     * @return Configured item.
     */
    private View configureLeagueMember(View convertView, ViewGroup parent, int position) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.leagues_screen_member, parent, false);

            // Set the view holder.
            convertView.setTag(new ViewHolderMember(convertView));
        }

        // Fetch view holder.
        ViewHolderMember viewHolder = (ViewHolderMember)convertView.getTag();

        viewHolder.mLeagueMemberRank.setText(Integer.toString(position + 1));
        viewHolder.mLeagueMemberUserName.setText(mMemberUserNames.get(position));
        viewHolder.mLeagueMemberWins.setText("W" + String.format("%03d", mMemberWins.get(position)));
        viewHolder.mLeagueMemberLosses.setText("L" + String.format("%03d", mMemberLosses.get(position)));
        viewHolder.mLeagueMemberRating.setText(Integer.toString(mMemberRating.get(position)));

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

    // region Inner Classes
    // ============================================================================================================

    static class ViewHolderHeader {

        @InjectView(R.id.leagues_summary_rank)    TextView mLeagueRank;
        @InjectView(R.id.leagues_summary_members) TextView mLeagueMembers;
        @InjectView(R.id.leagues_summary_rating)  TextView mLeagueRating;

        private ViewHolderHeader(View view) {

            // Map member variables.
            ButterKnife.inject(this, view);
        }
    }

    static class ViewHolderMember {

        @InjectView(R.id.leagues_member_rank)      TextView mLeagueMemberRank;
        @InjectView(R.id.leagues_member_user_name) TextView mLeagueMemberUserName;
        @InjectView(R.id.leagues_member_wins)      TextView mLeagueMemberWins;
        @InjectView(R.id.leagues_member_losses)    TextView mLeagueMemberLosses;
        @InjectView(R.id.leagues_member_rating)    TextView mLeagueMemberRating;

        private ViewHolderMember(View view) {

            // Map member variables.
            ButterKnife.inject(this, view);
        }
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks {

        public void onRecruiting(String leagueId, boolean recruiting);
    }

    // endregion
}