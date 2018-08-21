package com.flickr.app.data.prefs;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class LongPreference {

    private final SharedPreferences preferences;
    private final String key;
    private final int defaultValue;

    public LongPreference(@NonNull SharedPreferences preferences, @NonNull String key) {
        this(preferences, key, 0);
    }

    public LongPreference(@NonNull SharedPreferences preferences, @NonNull String key, int defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public long get() {
        return preferences.getLong(key, defaultValue);
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(long value) {
        preferences.edit().putLong(key, value).apply();
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}