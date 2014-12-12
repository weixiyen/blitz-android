package com.blitz.app.libraries.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mrkcsc on 12/11/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("unchecked, unused")
public class MGPreference<T> {

    private enum CommitType {
        BOOLEAN, INTEGER, STRING, STRING_MAP
    }

    private enum NativeCommitType {
        BOOLEAN, INTEGER, STRING
    }

    private Context context;
    private CommitType commitType;

    private T locallyCachedValue;

    private String key;

    /**
     * Creates a new preference object that is backed
     * by the android shared preferences object.
     */
    private MGPreference(Context context, String key, CommitType commitType) {

        this.context = context;
        this.key = key;
        this.commitType = commitType;
    }

    private MGPreference() { }

    /**
     * Create a boolean preference.
     */
    public static MGPreference<Boolean> createBoolean(Context context, String key) {

        return new MGPreference<>(context, key, MGPreference.CommitType.BOOLEAN);
    }

    /**
     * Create an integer preference.
     */
    public static MGPreference<Integer> createInteger(Context context, String key) {

        return new MGPreference<>(context, key, MGPreference.CommitType.INTEGER);
    }

    /**
     * Create a string preference.
     */
    public static MGPreference<String> createString(Context context, String key) {

        return new MGPreference<>(context, key, MGPreference.CommitType.STRING);
    }

    /**
     * Create a string map preference.
     */
    public static MGPreference<Map<String, String>> createStringMap(Context context, String key) {

        return new MGPreference<>(context, key, MGPreference.CommitType.STRING_MAP);
    }

    /**
     * Public preference setter, abstracts away the
     * native commit type and allows for persisting
     * more complex types such as dictionaries.
     */
    public void set(T value) {

        switch (commitType) {
            case BOOLEAN:
                set(value, NativeCommitType.BOOLEAN);
                break;
            case INTEGER:
                set(value, NativeCommitType.INTEGER);
                break;
            case STRING:
                set(value, NativeCommitType.STRING);
                break;
            case STRING_MAP:

                String jsonValue = new Gson().toJsonTree(value).toString();

                set(jsonValue, NativeCommitType.STRING);
                break;
        }
    }

    /**
     * Public preference getter.  Supports expanded commit types
     * backed by the native commit types supported by android.
     */
    public T get() {

        switch (commitType) {
            case BOOLEAN:
                return (T)get(NativeCommitType.BOOLEAN);
            case INTEGER:
                return (T)get(NativeCommitType.INTEGER);
            case STRING:
                return (T)get(NativeCommitType.STRING);
            case STRING_MAP:

                String jsonDictionary = (String)get(NativeCommitType.STRING);

                // Convert to dictionary.
                HashMap<String, String> dictionary =
                        new Gson().fromJson(jsonDictionary, new TypeToken<HashMap<String, String>>() {}.getType());

                // Convert to dictionary and return.
                return (T)(dictionary != null ? dictionary : new HashMap<>());
        }

        return null;
    }

    /**
     * Clear out the preference value from local
     * and native stores.
     */
    public void clear() {

        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);

        editor.remove(key);
        editor.apply();

        locallyCachedValue = null;
    }

    /**
     * Get an object from the native android preferences
     * manager, keyed by a native commit class type.
     */
    private Object get(NativeCommitType commitType) {

        if (locallyCachedValue != null) {

            return locallyCachedValue;
        }

        switch (commitType) {
            case BOOLEAN:
                return getSharedPreferences(context).getBoolean(key, false);
            case INTEGER:
                return getSharedPreferences(context).getInt(key, 0);
            case STRING:
                return getSharedPreferences(context).getString(key, null);
        }

        return null;
    }

    /**
     * Persists an object into the android shared
     * preferences, also clears locally cached value.
     */
    private void set(Object value, NativeCommitType commitType) {

        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);

        switch (commitType) {
            case BOOLEAN:
                editor.putBoolean(key, (Boolean)value);
                break;
            case INTEGER:
                editor.putInt(key, (Integer)value);
                break;
            case STRING:
                editor.putString(key, (String)value);
                break;
        }

        editor.apply();

        locallyCachedValue = null;
    }

    /**
     * Fetch the native android shared
     * preferences object.
     */
    private SharedPreferences getSharedPreferences(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Fetch and open the native android
     * shared preferences editor.
     */
    private SharedPreferences.Editor getSharedPreferencesEditor(Context context) {

        return getSharedPreferences(context).edit();
    }
}
