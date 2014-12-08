package com.blitz.app.rest_models;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;

/**
 * Created by mrkcsc on 9/9/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration, MismatchedQueryAndUpdateOfCollection")
public class RestModelItem extends RestModel {

    // region Member Variables
    // ============================================================================================================

    @SerializedName("id")            @Getter private String id;
    @SerializedName("title")         @Getter private String title;
    @SerializedName("description")   @Getter private String description;
    @SerializedName("item_type")     @Getter private String itemType;
    @SerializedName("is_restricted") @Getter private Boolean isRestricted;
    @SerializedName("price")         @Getter private Integer price;
    @SerializedName("img_paths")     @Getter private ArrayList<String> imgPaths;
    @SerializedName("last_updated")  @Getter private Date lastUpdated;
    @SerializedName("created")       @Getter private Date created;

    // endregion

    // region Public Methods
    // ============================================================================================================

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

    // region REST Methods
    // ============================================================================================================

    /**
     * Get an item.
     *
     * @param activity Activity for dialogs.
     * @param itemId Requested item.
     * @param callback Callback for success.
     */
    public static void fetchItem(Activity activity, String itemId, final RestResult<RestModelItem> callback) {

        restAPI.item_get(itemId, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResult()), null));
    }

    /**
     * Get a list of items.
     *
     * @param activity Activity for dialogs.
     * @param items Requested items.
     * @param limit Number of results.
     * @param callback Callback for success.
     */
    public static void fetchItems(Activity activity, List<String> items, Integer limit,
                                  @NonNull RestResults<RestModelItem> callback) {

        restAPI.items_get(items, "id", null, limit, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResults()), null));
    }

    /**
     * Get a list of items.
     *
     * @param activity Activity for dialogs.
     * @param items Requested items.
     * @param callback Callback for success.
     */
    public static void fetchItems(Activity activity, List<String> items,
                                  final RestResults<RestModelItem> callback) {

        fetchItems(activity, items, null, callback);
    }

    /**
     * Fetch avatar items owned by a user.
     *
     * @param activity Activity for dialogs.
     * @param userId User id.
     * @param callback Callback for success.
     */
    public static void fetchItemsOwnedByUser(Activity activity, String userId, final RestResults<RestModelItem> callback) {

        List<String> keys = new ArrayList<>();

        keys.add(userId);

        // Only get avatar types.
        String filter = "{ \"item_type\": \"AVATAR\"}";

        restAPI.items_get(keys, "user_id", filter, null, new RestAPICallback<>(activity,
                result -> callback.onSuccess(result.getResults()), null));
    }

    // endregion
}