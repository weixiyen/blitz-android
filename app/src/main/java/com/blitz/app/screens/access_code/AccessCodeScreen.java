package com.blitz.app.screens.access_code;

import android.content.Intent;

import com.blitz.app.R;
import com.blitz.app.base.activity.BaseActivity;
import com.blitz.app.models.objects.ObjectModelCode;
import com.blitz.app.models.operation.ModelOperation;
import com.blitz.app.screens.splash.SplashScreen;

import butterknife.OnClick;

/**
 * Created by Miguel Gaeta on 6/28/14.
 */
public class AccessCodeScreen extends BaseActivity {

    private ObjectModelCode mObjectModelCode;

    //==============================================================================================
    // Click Methods
    //==============================================================================================

    @OnClick(R.id.access_code_screen_continue_with_code) @SuppressWarnings("unused")
    public void haveCode() {

        mObjectModelCode = new ObjectModelCode("123456");
        mObjectModelCode.redeemCode(new ModelOperation() {

            @Override
            public void success() {
                super.success();

                if (mObjectModelCode.isValidCode()) {

                    // Transition to access code screen.
                    startActivity(new Intent(AccessCodeScreen.this, SplashScreen.class));
                }
            }
        });
    }
}