package com.blitz.app.utilities.cache;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * An (unfinished) sqlite backed cache implementation.
 *
 * Created by Nate on 8/25/14.
 */
class SqliteBackedMemoryCache implements Cache {

    final Map<String,String> map = new HashMap<String,String>();
    final CacheDbHelper mDbHelper;

    SqliteBackedMemoryCache(Context context) {
        mDbHelper = new CacheDbHelper(context);
    }


    @Override
    public boolean hasKey(String key) {

        return map.containsKey(key);
    }

    @Override
    public String get(String key) {

        return map.get(key);
    }

    @Override
    public void put(String key, String value) {

        map.put(key, value);
    }

    @Override
    public void clear() {

        map.clear();
    }
}
