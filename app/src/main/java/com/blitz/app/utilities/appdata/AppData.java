package com.blitz.app.utilities.appdata;

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
 * TODO: Improve security: https://github.com/sveinungkb/encrypted-userprefs
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
     * Fetch dictionary app data.
     *
     * @return Dictionary, or throws exception.
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> getDictionary() {

        return (HashMap<String, String>)get();
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
        } else

        if (mType == HashMap.class) {

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
        } else

        if (mType == HashMap.class) {

            // Fetch raw JSON string.
            String jsonDictionary = sharedPreferences.getString(mKey, null);

            // Convert to dictionary.
            HashMap<String, String> dictionary =
                    new Gson().fromJson(jsonDictionary,
                    new TypeToken<HashMap<String, String>>() {}.getType());

            // Convert to dictionary and return.
            return dictionary != null ? dictionary : new HashMap<String, String>();
        } else {

            throw new ClassCastException("AppDataObject is not of supported type.");
        }
    }
}