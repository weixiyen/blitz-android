package com.blitz.app.view_models;

import com.blitz.app.R;
import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelCallbacks;
import com.blitz.app.rest_models.RestModelGroup;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.logging.LogHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mrkcsc on 10/28/14. Copyright 2014 Blitz Studios
 */
public class ViewModelLeagues extends ViewModel {

    // region Member Variables
    // ============================================================================================================

    // Do not show more than six leagues.
    private static final int MAX_LEAGUES_TO_SHOW = 6;
    private static final int MAX_RECRUITING_LEAGUES_TO_SHOW = 100;

    // List of leagues this user belongs to.
    private List<RestModelGroup> mUserLeagues;

    // Track selected league.
    private String mSelectedLeagueId;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelLeagues(BaseActivity activity, Callbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Initialize the view model.
     */
    @Override
    public void initialize() {

        // Fetch league information.
        fetchUserLeaguesData();
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Fetch leagues user is associated with,
     * to a cap.
     */
    private void fetchUserLeaguesData() {

        RestModelGroup.getGroupsForUserId(mActivity, AuthHelper.instance().getUserId(),
                new RestModelCallbacks<RestModelGroup>() {

            @Override
            public void onSuccess(List<RestModelGroup> object) {

                if (object == null) {

                    // Empty user leagues array.
                    mUserLeagues = new ArrayList<RestModelGroup>();

                } else {

                    // Filter down leagues if necessary.
                    mUserLeagues = object.subList(0, object.size() > MAX_LEAGUES_TO_SHOW ?
                            MAX_LEAGUES_TO_SHOW : object.size());
                }

                List<String> leagueIds = new ArrayList<String>();
                List<String> leagueNames = new ArrayList<String>();

                for (RestModelGroup league : mUserLeagues) {

                    leagueIds.add(league.getId());
                    leagueNames.add(league.getName());
                }

                // Last is create.
                leagueIds.add(null);
                leagueNames.add(mActivity.getResources()
                        .getString(R.string.create_or_join_a_league));

                if (getCallbacks(Callbacks.class) != null) {
                    getCallbacks(Callbacks.class).onUserLeagues(leagueIds, leagueNames);
                }
            }
        });
    }

    /**
     * TODO: Implement.
     */
    private void fetchUserLeagueData() {

        RestModelGroup.getGroupWithId(null, mSelectedLeagueId, new RestModelCallback<RestModelGroup>() {

            @Override
            public void onSuccess(RestModelGroup object) {

                // Make sure we still want result.
                if (mSelectedLeagueId == null ||
                   !mSelectedLeagueId.equals(object.getId())) {

                    return;
                }

                // Fetch associated user objects.
                List<RestModelUser> users = new ArrayList<RestModelUser>(object.getMembers().values());

                // Sort the by rating.
                Collections.sort(users, new Comparator<RestModelUser>() {

                    @Override
                    public int compare(RestModelUser restModelUser1, RestModelUser restModelUser2) {
                        int user1Rating = restModelUser1.getRating();
                        int user2Rating = restModelUser2.getRating();

                        return user1Rating < user2Rating ?  1 :
                               user1Rating > user2Rating ? -1 : 0;
                    }
                });

                // Member information arrays.
                List<String>  memberUserIds   = new ArrayList<String>();
                List<String>  memberUserNames = new ArrayList<String>();
                List<Integer> memberWins      = new ArrayList<Integer>();
                List<Integer> memberLosses    = new ArrayList<Integer>();
                List<Integer> memberRating    = new ArrayList<Integer>();

                for (RestModelUser user : users) {

                    // Populate the arrays.
                    memberUserIds.add(user.getId());
                    memberUserNames.add(user.getUsername());
                    memberWins.add(user.getWins());
                    memberLosses.add(user.getLosses());
                    memberRating.add(user.getRating());
                }

                if (getCallbacks(Callbacks.class) != null) {
                    getCallbacks(Callbacks.class).onUserLeague(
                            object.getRank(), object.getRating(), object.getMemberIds().size(),
                            memberUserIds, memberUserNames, memberWins, memberLosses, memberRating);
                }
            }
        });
    }

    /**
     * Fetch the info related to panel that shows
     * for the create/join league screen.
     */
    private void fetchCreateJoinLeagueData() {

        // Fetch groups that are recruiting, no blocked UI.
        RestModelGroup.getGroupsRecruitingWithLimit(null, MAX_RECRUITING_LEAGUES_TO_SHOW,
                new RestModelCallbacks<RestModelGroup>() {

            @Override
            public void onSuccess(List<RestModelGroup> object) {

                // Make sure we still want result.
                if (mSelectedLeagueId != null) {

                    return;
                }

                // Fetch the current user id.
                String userId = AuthHelper.instance().getUserId();

                List<String>  leagueIds          = new ArrayList<String>();
                List<String>  leagueNames        = new ArrayList<String>();
                List<Integer> leagueRatings      = new ArrayList<Integer>();
                List<Integer> leagueMemberCounts = new ArrayList<Integer>();

                for (RestModelGroup league : object) {

                    // Only show leagues user is not already joined.
                    if (!league.getMemberIds().contains(userId)) {

                        leagueIds.add(league.getId());
                        leagueNames.add(league.getName());
                        leagueRatings.add(league.getRating());
                        leagueMemberCounts.add(league.getMemberIds().size());
                    }
                }

                // Emit recruiting leagues info.
                if (getCallbacks(Callbacks.class) != null) {
                    getCallbacks(Callbacks.class).onRecruitingLeagues
                            (leagueIds, leagueNames, leagueRatings, leagueMemberCounts);
                }
            }
        });
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Fetch either detailed information for
     * a specific view, or the information
     * for the create/join league view.
     *
     * @param selectedLeagueId Selected league id (can be null).
     */
    public void setSelectedLeagueId(String selectedLeagueId) {

        // Update selected league id.
        mSelectedLeagueId = selectedLeagueId;

        if (mSelectedLeagueId == null) {

            // Fetch create/join info.
            fetchCreateJoinLeagueData();
        } else {

            // Fetch individual league.
            fetchUserLeagueData();
        }
    }

    @SuppressWarnings("unused")
    public void createLeagueWithName(String leagueName) {

        // Create a group with user inputted name.
        RestModelGroup.createGroupWithName(mActivity, leagueName,
                new RestModelCallback<RestModelGroup>() {

            @Override
            public void onSuccess(RestModelGroup object) {

                if (getCallbacks(Callbacks.class) != null) {
                    getCallbacks(Callbacks.class).onLeagueCreated();
                }

                // Re-initialize model.
                initialize();
            }
        });
    }

    @SuppressWarnings("unused")
    public void joinLeagueWithName(String leagueName) {

        LogHelper.log("Join league: " + leagueName);

        RestModelGroup.getGroupWithName(mActivity, leagueName,
                new RestModelCallback<RestModelGroup>() {

            @Override
            public void onSuccess(RestModelGroup object) {

                LogHelper.log("Joined.");

                if (object != null) {

                    RestModelGroup.joinGroupWithId(mActivity, object.getId(),
                            new RestModelCallback<RestModelGroup>() {

                        @Override
                        public void onSuccess(RestModelGroup object) {

                            LogHelper.log("Super joined.");

                            if (getCallbacks(Callbacks.class) != null) {
                                getCallbacks(Callbacks.class).onLeagueJoined();
                            }

                            // Re-initialize model.
                            initialize();
                        }
                    });
                } else {

                    if (getCallbacks(Callbacks.class) != null) {
                        getCallbacks(Callbacks.class).onLeagueJoined();
                    }
                }
            }
        });
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onLeagueCreated();
        public void onLeagueJoined();

        public void onUserLeagues(List<String> leagueIds, List<String> leagueNames);

        public void onRecruitingLeagues(List<String> leagueIds,
                                        List<String> leagueNames,
                                        List<Integer> leagueRatings,
                                        List<Integer> leagueMemberCounts);

        public void onUserLeague(int leagueRank, int leagueRating, int leagueMembers,
                                 List<String>  memberUserIds,
                                 List<String>  memberUserNames,
                                 List<Integer> memberWins,
                                 List<Integer> memberLosses,
                                 List<Integer> memberRating);
    }

    // endregion
}