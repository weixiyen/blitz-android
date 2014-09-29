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

    // region Public Methods
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

                        List<String> avatarIds = new ArrayList<String>();
                        List<String> avatarUrls = new ArrayList<String>();

                        for (ObjectModelItem item : items) {

                            avatarIds.add(item.getId());
                            avatarUrls.add(item.getDefaultImgPath());
                        }

                        if (getCallbacks(ViewModelSettingsCallbacks.class) != null) {
                            getCallbacks(ViewModelSettingsCallbacks.class)
                                    .onAvatarsChanged(avatarIds, avatarUrls);
                        }
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
                    }
                }, true);
    }

    // endregion

    // region Callbacks Interface
    // =============================================================================================

    public interface ViewModelSettingsCallbacks extends ViewModelCallbacks {

        public void onEmailChanged(String email);
        public void onAvatarsChanged(List<String> avatarIds, List<String> avatarUrls);
    }

    // endregion
}
