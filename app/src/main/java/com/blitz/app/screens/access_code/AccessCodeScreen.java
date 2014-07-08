package com.blitz.app.screens.access_code;

import android.content.Intent;
import android.widget.EditText;

import com.blitz.app.R;
import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.models.objects.ObjectModelCode;
import com.blitz.app.models.operation.ModelOperation;
import com.blitz.app.screens.splash.SplashScreen;
import com.blitz.app.utilities.appdata.AppDataObject;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class AccessCodeScreen extends BaseActivity {

    @InjectView(R.id.access_code_screen_code) EditText mCode;

    private ObjectModelCode mObjectModelCode;

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.access_code_screen_continue_with_code) @SuppressWarnings("unused")
    public void haveCode() {

        if (ModelOperation.shouldThrottle()) {
            return;
        }

        if (mObjectModelCode == null) {
            mObjectModelCode = new ObjectModelCode();
        }

        mObjectModelCode.setValue(mCode.getText().toString());
        mObjectModelCode.redeemCode(new ModelOperation(this) {

            @Override
            public void success() {

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