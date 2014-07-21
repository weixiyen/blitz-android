package com.blitz.app.models.objects;

import android.app.Activity;

import com.blitz.app.models.rest.RestAPICallback;
import com.blitz.app.models.rest.RestAPIClient;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.models.rest_objects.JsonObjectQueue;

/**
 * Created by mrkcsc on 7/20/14.
 */
public class ObjectModelQueue extends ObjectModel {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    private String mDraftKey;

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    /**
     * Set the draft key (what kind of draft
     * are we joining).
     */
    public void setDraftKey(String draftKey) {

        mDraftKey = draftKey;
    }

    /**
     * Add user to draft queue.  Requires a draft
     * key to be set.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void queueUp(Activity activity, final QueueUpCallback callback) {

        // Define operation, call on queue up when complete.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success() {

                // Now in queue.
                callback.onQueueUp();
            }
        };

        // Construct POST body.
        JsonObjectQueue.Body body = new JsonObjectQueue.Body(mDraftKey);

        // Make api call.
        RestAPIClient.getAPI().queue
                (body, new RestAPICallback<JsonObjectQueue>(mRestApiObject, operation));
    }

    /**
     * Remove user from the draft queue.  Requires a
     * draft key to be set.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void leaveQueue(Activity activity, final LeaveQueueCallback callback) {

        // Operation callbacks.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success() {

                // Now left queue.
                callback.onLeaveQueue();
            }
        };

        // Make api call.
        RestAPIClient.getAPI().queue
                (mDraftKey, new RestAPICallback<JsonObjectQueue>(mRestApiObject, operation));
    }

    /**
     * Confirm user match in the draft queue. Requires
     * a draft key to be set.
     *
     * @param activity Activity for dialogs.
     * @param callback Callback.
     */
    public void confirmQueue(Activity activity, final ConfirmQueueCallback callback) {

        // Operation callbacks.
        RestAPIOperation operation = new RestAPIOperation(activity) {

            @Override
            public void success() {

                // Now confirmed queue.
                callback.onConfirmQueue();
            }
        };

        // Construct PUT body.
        JsonObjectQueue.BodyPUT body = new JsonObjectQueue.BodyPUT(mDraftKey);

        // Make api call.
        RestAPIClient.getAPI().queue
                (body, new RestAPICallback<JsonObjectQueue>(mRestApiObject, operation));
    }

    //==============================================================================================
    // Callbacks
    //==============================================================================================

    public interface QueueUpCallback      { public void onQueueUp();      }
    public interface LeaveQueueCallback   { public void onLeaveQueue();   }
    public interface ConfirmQueueCallback { public void onConfirmQueue(); }
}