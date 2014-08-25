package com.blitz.app.utilities.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.blitz.app.R;
import com.blitz.app.screens.loading.LoadingScreen;
import com.blitz.app.utilities.logging.LogHelper;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {

    // region Member Variables
    //==============================================================================================

    public static final int NOTIFICATION_ID = 1;

    // endregion

    // region Constructors
    //==============================================================================================

    /**
     * Default constructor,  The string passed to the
     * super method is used for debugging purposes only.
     */
    public GcmIntentService() {

        super(GcmIntentService.class.toString());
    }

    // endregion

    // region Overwritten Methods
    //==============================================================================================

    /**
     * Todo: Revise and make this method more robust.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        // Fetch instance of GCM services.
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // Fetch GCM message type parameter.
        String messageType = gcm.getMessageType(intent);

        // Fetch extras.
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) {

            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

                LogHelper.log("GCM error, please revise.");

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {


                // Post notification of received message.
                sendNotification(extras.getString("message", null));
            }
        }

        // Release wake lock provided by WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // endregion

    // region Private Methods
    //==============================================================================================

    /**
     * TODO: Supply the intent as a parameter (make this part of a reusable library).
     *
     * @param msg Target message from server.
     */
    private void sendNotification(String msg) {

        if (msg == null) {
            return;
        }

        String title = getResources().getString(R.string.app_name);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_blitz)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(msg);

        builder.setContentIntent( // supply an intent to send when the notification is clicked
                PendingIntent.getActivity( // retrieve an intent that will start a new activity
                        getApplicationContext(), 0,
                        new Intent(this, LoadingScreen.class),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        // Fetch instance of the notification manager.
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        // Push out our notification.
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // endregion
}