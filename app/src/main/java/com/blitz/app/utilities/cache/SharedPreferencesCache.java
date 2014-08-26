package com.blitz.app.utilities.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * A cache implementation backed by an Android SharedPreferences file. It may only store
 * values up to Preferences.MAX_VALUE_LENGTH.
 *
 * Created by Nate on 8/25/14.
 */
class SharedPreferencesCache implements Cache {

    private static final String FILE_NAME = "rest_cache";

    private final SharedPreferences mSharedPreferences;

    SharedPreferencesCache(Context context) {

        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    
    @Override
    public boolean hasKey(String key) {

        return mSharedPreferences.contains(key);
    }

    @Override
    public String get(String key) {

        return mSharedPreferences.getString(key, null);
    }

    /**
     *
     * @param key may not be null. May not exceed Preferences.MAX_KEY_LENGTH
     * @param value may not exceed Preferences.MAX_VALUE_LENGTH. May be null.
     */
    @Override
    public void put(String key, String value) {

        if(key != null && key.length() <= Preferences.MAX_KEY_LENGTH &&
                (value == null || value.length() <= Preferences.MAX_VALUE_LENGTH)) {

            mSharedPreferences.edit().putString(key, value).commit();
        }
    }

    @Override
    public void clear() {

        mSharedPreferences.edit().clear().commit();
    }
}
