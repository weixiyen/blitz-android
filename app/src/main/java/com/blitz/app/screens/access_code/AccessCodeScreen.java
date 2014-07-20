package com.blitz.app.screens.access_code;

import android.content.Intent;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.models.objects.ObjectModelCode;
import com.blitz.app.models.rest.RestAPIOperation;
import com.blitz.app.screens.splash.SplashScreen;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.app.AppDataObject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class AccessCodeScreen extends BaseActivity {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    @InjectView(R.id.access_code_screen_code) EditText mCode;

    private ObjectModelCode mObjectModelCode;

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.access_code_screen_continue_with_code) @SuppressWarnings("unused")
    public void continueWithCode() {

        if (RestAPIOperation.shouldThrottle()) {
            return;
        }

        if (mObjectModelCode == null) {
            mObjectModelCode = new ObjectModelCode();
        }

        // Provide code user inputted.
        mObjectModelCode.setValue(mCode.getText().toString());

        // Attempt to redeem it.
        mObjectModelCode.redeemCode(this, new ObjectModelCode.RedeemCodeCallback() {

            @Override
            public void onRedeemCode() {

                // If code is valid.
                if (mObjectModelCode.isValidCode()) {

                    // User now has access.
                    AppDataObject.hasAccess.set(true);

                    // Transition to splash screen, clear history.
                    startActivity(new Intent(AccessCodeScreen.this, SplashScreen.class), true);
                }
            }
        });
    }
}