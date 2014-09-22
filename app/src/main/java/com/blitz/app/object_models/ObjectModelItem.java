package com.blitz.app.object_models;

import android.app.Activity;

import com.blitz.app.utilities.json.JsonHelper;
import com.blitz.app.utilities.rest.RestAPICallbackCombined;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by mrkcsc on 9/9/14. Copyright 2014 Blitz Studios
 */
public class ObjectModelItem extends ObjectModel {

    // region Member Variables
    // =============================================================================================

    @SuppressWarnings("unused") private String mId;
    @SuppressWarnings("unused") private String mTitle;
    @SuppressWarnings("unused") private String mDescription;
    @SuppressWarnings("unused") private String mItemType;

    @SuppressWarnings("unused") private Boolean mIsRestricted;

    @SuppressWarnings("unused") private Integer mPrice;

    @SuppressWarnings("unused") private ArrayList<String> imgPaths;

    // endregion

    // region Public Methods
    // =============================================================================================

    /**
     * Get an item.
     *
     * @param activity Activity for dialogs.
     * @param itemId Requested item.
     * @param callback Callback for success.
     */
    public static void get(Activity activity, String itemId, final ItemCallback callback) {

        // Operation callbacks.
        RestAPICallbackCombined<JsonObject> operation =
                new RestAPICallbackCombined<JsonObject>(activity) {

            @Override
            public void success(JsonObject jsonObject) {

                // Create new object.
                ObjectModelItem objectModelItem = new ObjectModelItem();

                // Parse json into the object.
                parseItem(objectModelItem, jsonObject.getAsJsonObject("result"));

                // Now left queue.
                if (callback != null) {
                    callback.onSuccess(objectModelItem);
                }
            }
        };

        // Make api call.
        mRestAPI.item_get(itemId, operation);
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

    // region Private Methods
    // =============================================================================================

    /**
     * Parse a json object into a populated
     * item object model.
     *
     * @param item Item object.
     * @param jsonObject Json object.
     */
    private static void parseItem(ObjectModelItem item, JsonObject jsonObject) {

        if (item != null) {

            // Parse the strings.
            item.mId          = JsonHelper.parseString(jsonObject.get("id"));
            item.mTitle       = JsonHelper.parseString(jsonObject.get("title"));
            item.mDescription = JsonHelper.parseString(jsonObject.get("description"));
            item.mItemType    = JsonHelper.parseString(jsonObject.get("item_type"));

            // Parse the integers.
            item.mPrice = JsonHelper.parseInt(jsonObject.get("price"));

            // Parse the booleans.
            item.mIsRestricted = JsonHelper.parseBool(jsonObject.get("is_restricted"));

            // Parse the array lists.
            item.imgPaths = JsonHelper.parseArrayList(jsonObject.getAsJsonArray("img_paths"));
        }
    }

    // endregion

    // region Callbacks
    // =============================================================================================

    public interface ItemCallback {

        public void onSuccess(ObjectModelItem item);
    }

    // endregion
}