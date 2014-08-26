package com.blitz.app.utilities.cache;

import android.content.Context;

/**
 * Created by Nate on 8/25/14.
 */
public class CacheProvider {

    private Cache instance;

    synchronized public Cache get(Context context) {

        if(instance == null) {

            instance = new SharedPreferencesCache(context);
        }

        return instance;
    }
}
