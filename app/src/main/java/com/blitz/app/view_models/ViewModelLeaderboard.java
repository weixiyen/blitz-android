package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestModelGroup;
import com.blitz.app.rest_models.RestResults;
import com.blitz.app.rest_models.RestModelItem;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.utilities.android.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View model for leaderboard page.
 * Created by Nate on 9/30/14.
 */
public class ViewModelLeaderboard extends ViewModel {

    // region Member Variables
    // ============================================================================================================

    private static final int ELEMENTS_TO_FETCH = 150;

    public final static int TOP_150_PLAYERS = 0;
    public final static int TOP_150_LEAGUES = 1;

    public Integer mSelected;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     */
    public ViewModelLeaderboard(BaseActivity activity, ViewModel.Callbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // ============================================================================================================

    /**
     * Do a basic fetch of the top 150 users and return
     * the relevant data for the leaderboard.
     */
    @Override
    public void initialize() { }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Set selected leader board object type.
     */
    @SuppressWarnings("unused")
    public void setSelected(int selected) {

        if (mSelected != null && mSelected == selected) {

            return;
        }

        mSelected = selected;

        switch (mSelected) {

            case TOP_150_PLAYERS:
                fetchTopUsers();
                break;
            case TOP_150_LEAGUES:
                fetchTopLeagues();
                break;
        }
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Fetch top users.
     */
    private void fetchTopUsers() {

        // First grab the user objects.
        RestModelUser.getTopUsersWithLimit(null, ELEMENTS_TO_FETCH,
                new RestResults<RestModelUser>() {

                    @Override
                    public void onSuccess(final List<RestModelUser> object) {

                        final List<String> userIds = new ArrayList<>(object.size());
                        final List<String> userNames = new ArrayList<>(object.size());
                        final List<Integer> userWins = new ArrayList<>(object.size());
                        final List<Integer> userLosses = new ArrayList<>(object.size());
                        final List<Integer> userRatings = new ArrayList<>(object.size());
                        final List<String> userAvatarIds = new ArrayList<>(object.size());
                        final List<String> userAvatarUrls = new ArrayList<>(object.size());

                        for (RestModelUser user: object) {

                            // Pluck relevant data.
                            userIds.add(user.getId());
                            userNames.add(user.getUsername());
                            userWins.add(user.getWins());
                            userLosses.add(user.getLosses());
                            userRatings.add(user.getRating());
                            userAvatarIds.add(user.getAvatarId());
                        }

                        // Then fetch their item info (avatars).
                        RestModelItem.fetchItems(null, userAvatarIds, ELEMENTS_TO_FETCH,
                                items -> {

                                    if (mSelected != TOP_150_PLAYERS) {

                                        return;
                                    }

                                    Map<String, String> itemUrls = new HashMap<>();

                                    // For each unique item.
                                    for (RestModelItem item: items) {

                                        // Convert into a map of id to image path.
                                        if (!itemUrls.containsKey(item.getId())) {
                                            itemUrls.put(item.getId(), item.getDefaultImgPath());
                                        }
                                    }

                                    // Now or each avatar id.
                                    for (String avatarId : userAvatarIds) {

                                        // Populate associated avatar url.
                                        userAvatarUrls.add(itemUrls.get(avatarId));
                                    }

                                    if (getCallbacks(Callbacks.class) != null) {
                                        getCallbacks(Callbacks.class)
                                                .onLeadersReceived(userIds, userNames, userWins,
                                                        userLosses, userRatings, userAvatarUrls);
                                    }
                                });
                    }
                });
    }

    /**
     * Fetch the top leagues.
     */
    private void fetchTopLeagues() {

        RestModelGroup.getTopGroupsWithLimit(null, ELEMENTS_TO_FETCH, new RestResults<RestModelGroup>() {

            @Override
            public void onSuccess(List<RestModelGroup> object) {

                if (mSelected != TOP_150_LEAGUES) {

                    return;
                }

                final List<String>  leagueIds     = new ArrayList<>(object.size());
                final List<String>  leagueNames   = new ArrayList<>(object.size());
                final List<Integer> leagueRatings = new ArrayList<>(object.size());

                for (RestModelGroup group: object) {

                    // Pluck relevant data.
                    leagueIds.add(group.getId());
                    leagueNames.add(group.getName());
                    leagueRatings.add(group.getRating());
                }

                if (getCallbacks(Callbacks.class) != null) {
                    getCallbacks(Callbacks.class)
                            .onLeaguesReceived(leagueIds, leagueNames, leagueRatings);
                }
            }
        });
    }

    // endregion

    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onLeadersReceived(List<String>  userIds,
                                      List<String>  userNames,
                                      List<Integer> userWins,
                                      List<Integer> userLosses,
                                      List<Integer> userRating,
                                      List<String>  userAvatarUrls);

        public void onLeaguesReceived(List<String>  leagueIds,
                                      List<String>  leagueNames,
                                      List<Integer> leagueRating);
    }

    // endregion
}
