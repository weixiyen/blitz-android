package com.blitz.app.utilities.facebook;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Miguel Gaeta on 5/31/14. Encapsulates
 * common facebook SDK functionality.
 */
@SuppressWarnings("unused")
public class FacebookHelper {

    private Activity mActivity;
    private Session mSession;

    public FacebookHelper(Activity activity) {
        mActivity = activity;
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Authorize a user through facebook SDK.
     *
     * @param callback Callback method.
     */
    public void authorizeUser(final Request.GraphUserCallback callback) {

        // Start Facebook Login
        Session.openActiveSession(mActivity, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(final Session session, SessionState state, Exception exception) {

                mSession = session;

                // If session opened.
                if (mSession.isOpened()) {

                    // Make request to the /me API.
                    Request.newMeRequest(session, callback).executeAsync();
                }
            }
        });
    }

    /**
     * Is user authorized through facebook.
     */
    public boolean isUserLoggedIntoFacebook() {

        // Try to fetch facebook session object.
        Session session = Session.getActiveSession();

        // If opened, user already logged in.
        return session != null && session.isOpened();
    }

    /**
     * Fetch access token.
     *
     * @return Facebook access token,
     * or null if graph user does not exist.
     */
    public String getAccessToken(GraphUser graphUser) {
        if (graphUser != null) {
            return mSession.getAccessToken();
        }

        return null;
    }

    /**
     * Fetch key hash for the app.
     *
     * @return Key hash, null if not found or error.
     */
    public String getFacebookKeyHash() {

        try {

            // Fetch package name and info (for current app).
            String packageName = mActivity.getApplicationContext().getPackageName();
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            // If at least one signature.
            if (packageInfo.signatures.length > 0) {

                // Pull the first.
                Signature signature = packageInfo.signatures[0];

                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());

                // Fetch key hash from the message digest.
                return Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT);
            }
        }

        catch (PackageManager.NameNotFoundException ignored) { }
        catch (NoSuchAlgorithmException ignored)             { }

        return null;
    }
}