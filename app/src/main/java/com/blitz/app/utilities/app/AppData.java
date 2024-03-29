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

    // region Member Variables
    // ============================================================================================================

    // Application context.
    private static Context mContext;

    // Data key.
    final String mKey;
    // TODO: cache the value locally as private T mVal ?

    private final Class mClass;

    // endregion

    // region Constructor
    // ============================================================================================================

    /**
     * Cannot be instantiated. Use factory methods instead.
     */
    private AppData(String key, Class classObject) {

        mKey = key;
        mClass = classObject;
    }

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Initialize with an application context
     * so that it can be available statically.
     *
     * @param context Application context.
     */
    @SuppressWarnings("unused")
    public static void init(Context context) {

        mContext = context;
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
    @SuppressWarnings("unused")
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

        if (mClass == String.class) {

            editor.putString(mKey, (String)value);
        } else if (mClass == Boolean.class) {

            editor.putBoolean(mKey, (Boolean)value);
        } else if (mClass == Integer.class) {

            editor.putInt(mKey, (Integer)value);
        } else if (mClass == HashMap.class) {

            // Convert dictionary into JSON string.
            String newValue = new Gson().toJsonTree(value).toString();

            // Persist into editor.
            editor.putString(mKey, newValue);
        } else {

            throw new ClassCastException("AppDataObject is not of supported type.");
        }

        editor.apply();
    }

    // endregion

    // region Package Methods
    // ============================================================================================================

    /**
     * Convenience method to get shared preferences
     */
    final SharedPreferences getSharedPreferences() {

        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    /**
     * Factory method to return a string data object
     */
    static AppData<String> string(String key) {

        return new AppData<String>(key, String.class) {

            @Override
            public String get() {

                return getSharedPreferences().getString(this.mKey, null);
            }
        };
    }

    /**
     * Factory method to return a boolean data object
     */
    static AppData<Boolean> bool(String key) {

        return new AppData<Boolean>(key, Boolean.class) {

            @Override
            public Boolean get() {

                return getSharedPreferences().getBoolean(this.mKey, false);
            }
        };
    }

    /**
     * Factory method to return an integer data object
     */
    static AppData<Integer> integer(String key) {

        return new AppData<Integer>(key, Integer.class) {

            @Override
            public Integer get() {

                return getSharedPreferences().getInt(this.mKey, 0);
            }
        };
    }

    /**
     * Factory method to return a "dictionary" data object
     */
    static AppData<HashMap<String, String>> dictionary(String key) {

        return new AppData<HashMap<String, String>>(key, HashMap.class) {

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

    // endregion

    // region Private Methods
    // ============================================================================================================

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

    // endregion
}