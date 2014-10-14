package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestModelCallback;
import com.blitz.app.rest_models.RestModelItem;
import com.blitz.app.rest_models.RestModelUser;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nate on 9/28/14. Copyright 2014 Blitz Studios
 */
public class ViewModelSettings extends ViewModel {

    // region Member Variables
    // =============================================================================================

    // List of the avatars this user owns.
    private List<RestModelItem> mUserAvatars;

    // The current avatar of this user.
    private String mUserAvatarIdCurrent;

    // endregion

    // region Constructor
    // =============================================================================================

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelSettings(BaseActivity activity, ViewModel.Callbacks callbacks) {
        super(activity, callbacks);
    }

    // endregion

    // region Overwritten Methods
    // =============================================================================================

    /**
     * Fetch info and setup the view model.
     */
    @Override
    public void initialize() {

        fetchUserInfo();
        fetchUserAvatars();
    }

    // endregion


    // region Public Methods
    // =============================================================================================

    /**
     * Update the user avatar.
     */
    public void updateUserAvatar(String itemAvatarId) {

        // Silently update the users avatar.
        RestModelUser.updateAvatar(null, itemAvatarId, null);
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Fetch avatars owned by the user.
     */
    private void fetchUserAvatars() {

        // Fetch avatars owned by this user.
        RestModelItem.fetchItemsOwnedByUser(mActivity, AuthHelper.instance().getUserId(),
                new RestModelItem.CallbackItems() {

                    @Override
                    public void onSuccess(List<RestModelItem> items) {

                        mUserAvatars = items;

                        trySyncAvatars();
                    }
                });
    }

    /**
     * Fetch relevant user information that can be
     * modified in the settings.
     */
    private void fetchUserInfo() {

        // Fetch user information.
        RestModelUser.getUser(mActivity, AuthHelper.instance().getUserId(),
                new RestModelCallback<RestModelUser>() {

                    @Override
                    public void onSuccess(RestModelUser user) {

                        if (getCallbacks(Callbacks.class) != null) {
                            getCallbacks(Callbacks.class).onEmailChanged(user.getEmail());
                        }

                        // Fetch associated item model.
                        RestModelItem.fetchItem(mActivity, user.getAvatarId(),
                                new RestModelItem.CallbackItem() {

                                    @Override
                                    public void onSuccess(RestModelItem item) {

                                        mUserAvatarIdCurrent = item.getId();

                                        trySyncAvatars();
                                    }
                                });
                    }
                }, true);
    }

    /**
     * Need both user avatars and the current avatar
     * id before we can emit information to receiver.
     */
    private void trySyncAvatars() {

        // If we have user avatars and the current avatar id.
        if (mUserAvatars != null && mUserAvatarIdCurrent != null) {

            List<String> avatarIds = new ArrayList<String>();
            List<String> avatarUrls = new ArrayList<String>();

            for (RestModelItem item : mUserAvatars) {

                avatarIds.add(item.getId());
                avatarUrls.add(item.getDefaultImgPath());
            }

            if (getCallbacks(Callbacks.class) != null) {
                getCallbacks(Callbacks.class)
                        .onAvatarsChanged(avatarIds, avatarUrls, mUserAvatarIdCurrent);
            }
        }
    }

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onEmailChanged(String email);
        public void onAvatarsChanged(List<String> userAvatarIds,
                                     List<String> userAvatarUrls, String userAvatarId);
    }

    // endregion
}
