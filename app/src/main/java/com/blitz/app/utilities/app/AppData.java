package com.blitz.app.utilities.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Created by mrkcsc on 7/7/14.
 *
 * TODO: Not thread safe. Either use ThreadLocal or synchronized, or something more clever.
 *
 * TODO: Improve security: https://github.com/sveinungkb/encrypted-userprefs
 */
public abstract class AppData<T> {

    //==============================================================================================
    // Constructors
    //==============================================================================================

    // Application context.
    private static Context mContext;

    // Data key.
    final String mKey;
    // TODO: cache the value locally as private T mVal ?

    //==============================================================================================
    // Constructors
    //==============================================================================================

    /**
     * Cannot be instantiated. Use factory methods instead.
     */
    private AppData(String key) {
        // Set key.
        mKey = key;
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Initialize with an application context
     * so that it can be available statically.
     *
     * @param context Application context.
     */
    public static void init(Context context) {

        mContext = context;
    }

    final SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    /**
     * Factory method to return a string data object
     */
    public static AppData<String> string(String key) {
        return new AppData<String>(key) {
            @Override
            public String get() {
                return getSharedPreferences().getString(this.mKey, null);
            }
        };
    }

    /**
     * Factory method to return a boolean data object
     */
    public static AppData<Boolean> bool(String key) {
        return new AppData<Boolean>(key) {
            @Override
            public Boolean get() {
                return getSharedPreferences().getBoolean(this.mKey, false);
            }
        };
    }

    /**
     * Factory method to return an integer data object
     */
    public static AppData<Integer> integer(String key) {
        return new AppData<Integer>(key) {
            @Override
            public Integer get() {
                return getSharedPreferences().getInt(this.mKey, 0);
            }
        };
    }

    /**
     * Factory method to return a "dictionary" data object
     */
    public static AppData<HashMap<String, String>> dictionary(String key) {
        return new AppData<HashMap<String, String>>(key) {
            @Override
            public HashMap<String, String> get() {
                // Fetch raw JSON string.
                String jsonDictionary = getSharedPreferences().getString(this.mKey, null);

                // Convert to dictionary.
                HashMap<String, String> dictionary =
                        new Gson().fromJson(jsonDictionary,
                                new TypeToken<HashMap<String, String>>() {
                                }.getType());

                // Convert to dictionary and return.
                return (dictionary != null ? dictionary : new HashMap<String, String>());
            }
        };
    }

    /**
     * Clear all app data.
     */
    @SuppressWarnings("unused") @SuppressLint("CommitPrefEdits")
    public static void clear() {

        // Get editor.
        SharedPreferences.Editor editor = getEditor();

        editor.clear();
        editor.apply();
    }

    /**
     * Clear app data for specific app data object.
     *
     * @param appData App data object.
     */
    @SuppressWarnings("unused") @SuppressLint("CommitPrefEdits")
    public static void clear(AppData appData) {

        // Get editor.
        SharedPreferences.Editor editor = getEditor();

        editor.remove(appData.mKey);
        editor.apply();
    }

    /**
     *  Get app data value.
     *
     * @return Supported value.
     */
    public abstract T get();

    /**
     * Set app data object, must be one
     * of the supported primitive types.
     *
     * @param value New value.
     */
    @SuppressLint("CommitPrefEdits")
    public void set(T value) {

        // Get editor.
        SharedPreferences.Editor editor = getEditor();

        if (value instanceof String) {

            editor.putString(mKey, (String)value);
        } else

        if (value instanceof Boolean) {

            editor.putBoolean(mKey, (Boolean)value);
        } else

        if (value instanceof Integer) {

            editor.putInt(mKey, (Integer)value);
        } else

        if (value instanceof Float) {

            editor.putFloat(mKey, (Float)value);
        } else

        if (value instanceof Long) {

            editor.putLong(mKey, (Long)value);
        } else

        if (value instanceof HashMap) {

            // Convert dictionary into JSON string.
            String newValue = new Gson().toJsonTree(value).toString();

            // Persist into editor.
            editor.putString(mKey, newValue);

        } else {

            throw new ClassCastException("AppDataObject is not of supported type.");
        }

        editor.apply();
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Fetch android preferences editor.
     *
     * @return Preferences editor.
     */
    private static SharedPreferences.Editor getEditor() {

        // Fetch shared preferences.
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        // Open for editing.
        return sharedPreferences.edit();
    }

}