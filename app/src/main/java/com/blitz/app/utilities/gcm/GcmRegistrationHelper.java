package com.blitz.app.utilities.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by mrkcsc on 8/6/14.
 */
public class GcmRegistrationHelper {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Request tag for asking user to obtain google play services.
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    @SuppressWarnings("unused")
    private static boolean checkPlayServices(Activity activity) {

        // Check to see if google play services available on this device.
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                activity.finish();
            }

            return false;
        }

        return true;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    @SuppressWarnings("unused")
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            // Should never happen.
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}