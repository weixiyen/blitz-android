package com.blitz.app.utilities.appdata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mrkcsc on 7/7/14.
 */
public class AppData {

    //==============================================================================================
    // Constructors
    //==============================================================================================

    // Application context.
    private static Context mContext;

    // Data type and key.
    private Class mType;
    private String mKey;

    //==============================================================================================
    // Constructors
    //==============================================================================================

    @SuppressWarnings("unused")
    protected AppData() {

    }

    @SuppressWarnings("unused")
    protected AppData(Class type, String key) {

        // Set type.
        mType = type;

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

    /**
     * Clear all app data.
     */
    @SuppressLint("CommitPrefEdits")
    public static void clear() {

        // Fetch shared preferences.
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        // Open for editing.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.apply();
    }

    /**
     * Fetch boolean app data.
     *
     * @return Boolean, or throws exception on user error.
     */
    public boolean getBoolean() {

        return (Boolean)get();
    }

    /**
     * Set app data object, must be one
     * of the supported primitive types.
     *
     * @param value New value.
     */
    @SuppressLint("CommitPrefEdits")
    public void set(Object value) {

        // Fetch shared preferences.
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        // Open for editing.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (mType == String.class) {

            editor.putString(mKey, (String)value);
        } else

        if (mType == Boolean.class) {

            editor.putBoolean(mKey, (Boolean)value);
        } else

        if (mType == Integer.class) {

            editor.putInt(mKey, (Integer)value);
        } else

        if (mType == Float.class) {

            editor.putFloat(mKey, (Float)value);
        } else

        if (mType == Long.class) {

            editor.putLong(mKey, (Long)value);
        } else {

            throw new ClassCastException("AppDataObject is not of supported type.");
        }

        editor.apply();
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     *  Get app data value.
     *
     * @return Supported value.
     */
    private Object get() {

        // Fetch shared preferences.
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        if (mType == String.class) {

            return sharedPreferences.getString(mKey, null);
        } else

        if (mType == Boolean.class) {

            return sharedPreferences.getBoolean(mKey, false);
        } else

        if (mType == Integer.class) {

            return sharedPreferences.getInt(mKey, 0);
        } else

        if (mType == Float.class) {

            return sharedPreferences.getFloat(mKey, 0f);
        } else

        if (mType == Long.class) {

            return sharedPreferences.getLong(mKey, 0);
        } else {

            throw new ClassCastException("AppDataObject is not of supported type.");
        }
    }
}