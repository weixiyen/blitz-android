/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blitz.app.utilities.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.blitz.app.R;
import com.blitz.app.screens.main.MainScreen;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
@SuppressWarnings("ALL")
public class GcmIntentService extends IntentService {
    
    public static final int NOTIFICATION_ID = 1;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM GcmIntentService"; // Tag used to identify log lines

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                sendNotification(extras.getString("message", "New content"));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {

        int icon = R.drawable.icon_blitz;
        long when = System.currentTimeMillis();
        String title = "Blitz";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentText(msg);

        /*
        // back navigation stuff (doesn't seem to work with old android api versions)
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainScreen.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentIntent(resultPendingIntent);
        */

        // TODO, supply the intent as a parameter (make this part of a reusable library)
        builder.setContentIntent( // supply an intent to send when the notification is clicked
                PendingIntent.getActivity( // retrieve an intent that will start a new activity
                        this.getApplicationContext(), 0,
                        new Intent(this, MainScreen.class), // intent that starts the main screen
                        PendingIntent.FLAG_UPDATE_CURRENT));

        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
