package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestModelCallbacks;
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
    public void initialize() {

        final int usersToFetch = 150;

        // First grab the user objects.
        RestModelUser.getTopUsersWithLimit(mActivity, usersToFetch,
                new RestModelCallbacks<RestModelUser>() {

                    @Override
                    public void onSuccess(final List<RestModelUser> object) {

                        final List<String> userIds =
                                new ArrayList<String>(object.size());
                        final List<String> userNames =
                                new ArrayList<String>(object.size());
                        final List<Integer> userWins =
                                new ArrayList<Integer>(object.size());
                        final List<Integer> userLosses =
                                new ArrayList<Integer>(object.size());
                        final List<Integer> userRatings =
                                new ArrayList<Integer>(object.size());
                        final List<String> userAvatarIds =
                                new ArrayList<String>(object.size());
                        final List<String> userAvatarUrls =
                                new ArrayList<String>(object.size());

                        for (RestModelUser user: object) {

                            // Pluck relevant data.
                            userIds.add(user.getId());
                            userNames.add(user.getUsername());
                            userWins.add(user.getWins());
                            userLosses.add(user.getLosses());
                            userRatings.add(user.getRating());

                            // Add unique avatar id's.
                            //if (!userAvatarIds.contains(user.getAvatarId())) {
                                 userAvatarIds.add(user.getAvatarId());
                            //}
                        }

                        // Then fetch their item info (avatars).
                        RestModelItem.fetchItems(mActivity, userAvatarIds, usersToFetch,
                                new RestModelItem.CallbackItems() {

                            @Override
                            public void onSuccess(List<RestModelItem> items) {

                                Map<String, String> itemUrls = new HashMap<String, String>();

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
                            }
                        });
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
    }

    // endregion
}
