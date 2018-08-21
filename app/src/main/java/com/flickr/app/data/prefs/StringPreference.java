package com.flickr.app.data.prefs;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class StringPreference {

    private final SharedPreferences preferences;
    private final String key;
    private final String defaultValue;

    public StringPreference(@NonNull SharedPreferences preferences, @NonNull String key) {
        this(preferences, key, "");
    }

    public StringPreference(@NonNull SharedPreferences preferences, @NonNull String key, @Nullable String defaultValue) {
        this.preferences = preferences;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Nullable
    public String get() {
        return preferences.getString(key, defaultValue);
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void set(@NonNull String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }
}