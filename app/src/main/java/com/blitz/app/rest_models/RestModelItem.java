package com.blitz.app.rest_models;

import android.app.Activity;

import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mrkcsc on 9/9/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class RestModelItem extends RestModel {

    // region Member Variables
    // =============================================================================================

    @SuppressWarnings("unused") @SerializedName("id")
    private String mId;
    @SuppressWarnings("unused") @SerializedName("title")
    private String mTitle;
    @SuppressWarnings("unused") @SerializedName("description")
    private String mDescription;
    @SuppressWarnings("unused") @SerializedName("item_type")
    private String mItemType;

    @SuppressWarnings("unused") @SerializedName("is_restricted")
    private Boolean mIsRestricted;

    @SuppressWarnings("unused") @SerializedName("price")
    private Integer mPrice;

    @SuppressWarnings("unused") @SerializedName("img_paths")
    private ArrayList<String> imgPaths;

    @SuppressWarnings("unused") @SerializedName("last_updated")
    private Date mLastUpdated;
    @SuppressWarnings("unused") @SerializedName("created")
    private Date mCreated;

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Fetch item id.
     *
     * @return Item id.
     */
    public String getId() {

        return mId;
    }

    /**
     * Get a default image path.
     */
    public String getDefaultImgPath() {

        // Return the first image path.
        if (imgPaths != null && imgPaths.size() > 0) {

            return imgPaths.get(0);
        }

        return null;
    }

    // endregion

    // region Rest Methods
    // =============================================================================================

    /**
     * Get an item.
     *
     * @param activity Activity for dialogs.
     * @param itemId Requested item.
     * @param callback Callback for success.
     */
    @SuppressWarnings("unused")
    public static void fetchItem(Activity activity, String itemId, final CallbackItem callback) {

        RestAPICallback<RestAPIResult<RestModelItem>> operation =
                new RestAPICallback<RestAPIResult<RestModelItem>>(activity) {

                    @Override
                    public void success(RestAPIResult<RestModelItem> jsonObject) {

                        // Now left queue.
                        if (callback != null) {
                            callback.onSuccess(jsonObject.getResult());
                        }
                    }
                };

        mRestAPI.item_get(itemId, operation);
    }

    /**
     * Get a list of items.
     *
     * @param activity Activity for dialogs.
     * @param items Requested items.
     * @param limit Number of results.
     * @param callback Callback for success.
     */
    @SuppressWarnings("unused")
    public static void fetchItems(Activity activity, List<String> items, Integer limit,
                                  final CallbackItems callback) {

        RestAPICallback<RestAPIResult<RestModelItem>> operation =
                new RestAPICallback<RestAPIResult<RestModelItem>>(activity) {

            @Override
            public void success(RestAPIResult<RestModelItem> jsonObject) {

                if (callback != null) {
                    callback.onSuccess(jsonObject.getResults());
                }
            }
        };

        mRestAPI.items_get(items, "id", null, limit, operation);
    }

    /**
     * Get a list of items.
     *
     * @param activity Activity for dialogs.
     * @param items Requested items.
     * @param callback Callback for success.
     */
    @SuppressWarnings("unused")
    public static void fetchItems(Activity activity, List<String> items,
                                  final CallbackItems callback) {

        fetchItems(activity, items, null, callback);
    }

    /**
     * Fetch avatar items owned by a user.
     *
     * @param activity Activity for dialogs.
     * @param userId User id.
     * @param callback Callback for success.
     */
    @SuppressWarnings("unused")
    public static void fetchItemsOwnedByUser(Activity activity, String userId, final CallbackItems callback) {

        RestAPICallback<RestAPIResult<RestModelItem>> operation =
                new RestAPICallback<RestAPIResult<RestModelItem>>(activity) {

                    @Override
                    public void success(RestAPIResult<RestModelItem> jsonObject) {

                        if (callback != null) {
                            callback.onSuccess(jsonObject.getResults());
                        }
                    }
                };

        List<String> keys = new ArrayList<String>();

        keys.add(userId);

        // Only get avatar types.
        String filter = "{ \"item_type\": \"AVATAR\"}";

        mRestAPI.items_get(keys, "user_id", filter, null, operation);
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface CallbackItem {

        public void onSuccess(RestModelItem item);
    }

    public interface CallbackItems {

        public void onSuccess(List<RestModelItem> items);
    }

    // endregion
}