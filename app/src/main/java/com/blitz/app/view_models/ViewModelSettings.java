package com.blitz.app.view_models;

import android.app.Activity;

import com.blitz.app.object_models.ObjectModelItem;
import com.blitz.app.object_models.ObjectModelUser;
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
    private List<ObjectModelItem> mUserAvatars;

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
    public ViewModelSettings(Activity activity, ViewModelCallbacks callbacks) {
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
        ObjectModelUser.updateAvatar(null, itemAvatarId, null);
    }

    // endregion

    // region Private Methods
    // =============================================================================================

    /**
     * Fetch avatars owned by the user.
     */
    private void fetchUserAvatars() {

        // Fetch avatars owned by this user.
        ObjectModelItem.fetchItemsOwnedByUser(mActivity, AuthHelper.instance().getUserId(),
                new ObjectModelItem.CallbackItems() {

                    @Override
                    public void onSuccess(List<ObjectModelItem> items) {

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
        ObjectModelUser.getUser(mActivity, AuthHelper.instance().getUserId(),
                new ObjectModelUser.CallbackUser() {

                    @Override
                    public void onSuccess(ObjectModelUser user) {

                        if (getCallbacks(ViewModelSettingsCallbacks.class) != null) {
                            getCallbacks(ViewModelSettingsCallbacks.class)
                                    .onEmailChanged(user.getEmail());
                        }

                        // Fetch associated item model.
                        ObjectModelItem.fetchItem(mActivity, user.getAvatarId(),
                                new ObjectModelItem.CallbackItem() {

                                    @Override
                                    public void onSuccess(ObjectModelItem item) {

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

            for (ObjectModelItem item : mUserAvatars) {

                avatarIds.add(item.getId());
                avatarUrls.add(item.getDefaultImgPath());
            }

            if (getCallbacks(ViewModelSettingsCallbacks.class) != null) {
                getCallbacks(ViewModelSettingsCallbacks.class)
                        .onAvatarsChanged(avatarIds, avatarUrls, mUserAvatarIdCurrent);
            }
        }
    }

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    public interface ViewModelSettingsCallbacks extends ViewModelCallbacks {

        public void onEmailChanged(String email);
        public void onAvatarsChanged(List<String> userAvatarIds,
                                     List<String> userAvatarUrls, String userAvatarId);
    }

    // endregion
}
