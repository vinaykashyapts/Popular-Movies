package com.vk.udacitynanodegree.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Created by Vinay on 4/11/15.
 */
public class PreferenceManager {
    public static final String TAG = "NanodegreeVkPref";

    private static PreferenceManager mInstance = null;

    private final SharedPreferences preferences;

    private PreferenceManager(Context context) {
        preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }

    /**
     * The global default {@link PreferenceManager} instance.
     */
    public static PreferenceManager with(Context context) {
        if (mInstance == null) {
            synchronized (PreferenceManager.class) {
                if (mInstance == null) {
                    mInstance = new Builder(context).build();
                }
            }
        }
        return mInstance;
    }

    /**
     * Fluent API for creating {@link PreferenceManager} instances.
     */
    private static class Builder {
        private final Context context;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        /**
         * Create the {@link PreferenceManager} instance.
         */
        public PreferenceManager build() {
            return new PreferenceManager(context);
        }
    }

    /**
     * Retrieve all values from the preferences.
     * <p>Note that you <em>must not</em> modify the collection returned
     * by this method, or alter any of its contents.  The consistency of your
     * stored data is not guaranteed if you do.
     *
     * @return Returns a map containing a list of pairs key/value representing the preferences.
     * @throws NullPointerException
     */
    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not a String.
     * @throws ClassCastException
     */
    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    /**
     * Retrieve a set of String values from the preferences.
     * <p>Note that you <em>must not</em> modify the set instance returned
     * by this call.  The consistency of the stored data is not guaranteed
     * if you do, nor is your ability to modify the instance at all.
     *
     * @param key       The name of the preference to retrieve.
     * @param defValues Values to return if this preference does not exist.
     * @return Returns the preference values if they exist, or defValues.
     * Throws ClassCastException if there is a preference with this name that is not a Set.
     * @throws ClassCastException
     */
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return preferences.getStringSet(key, defValues);
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not an int.
     * @throws ClassCastException
     */
    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not a long.
     * @throws ClassCastException
     */
    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    /**
     * Retrieve a float value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not a float.
     * @throws ClassCastException
     */
    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not a boolean.
     * @throws ClassCastException
     */
    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key The name of the preference to check.
     * @return Returns true if the preference exists in the preferences,
     * otherwise false.
     */
    public boolean contains(String key) {
        return preferences.contains(key);
    }

    /**
     * Create a new Editor for these preferences, through which you can make
     * modifications to the data in the preferences and atomically commit those
     * changes back to the SharedPreferences object.
     * <p>Note that you <em>must</em> call {@link SharedPreferences.Editor#apply} to have any
     * changes you perform in the Editor actually show up in the SharedPreferences.
     *
     * @return Returns a new instance of the {@link SharedPreferences.Editor} interface, allowing
     * you to modify the values in this SharedPreferences object.
     */
    public SharedPreferences.Editor edit() {
        return preferences.edit();
    }

    /**
     * Registers a callback to be invoked when a change happens to a preference.
     *
     * @param listener The callback that will run.
     * @see #unregisterOnSharedPreferenceChangeListener
     */
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregisters a previous callback.
     *
     * @param listener The callback that should be unregistered.
     * @see #registerOnSharedPreferenceChangeListener
     */
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}
