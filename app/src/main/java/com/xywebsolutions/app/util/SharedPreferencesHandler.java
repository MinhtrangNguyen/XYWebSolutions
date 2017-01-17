package com.xywebsolutions.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Dung Nguyen on 3/8/16.
 */
public class SharedPreferencesHandler {
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AppSettings";

    // Context
    private Context mContext;


    public void resetPreference(){
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }
    public SharedPreferencesHandler(Context context) {
        this.mContext = context;
    }

    // ---- FOR SETTING DATA ----
    public void setPreferences(String key, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public void setPreferences(String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public void setPreferences(String key, long value) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        Editor edit = sp.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public void setPreferences(String key, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public void setPreferences(String key, Float value) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        Editor edit = sp.edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    // ---- FOR GETTING DATA ----
    public boolean getBooleanSavedPreferences(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        boolean value = sp.getBoolean(key, false);
        return value;
    }

    public String getStringSavedPreferences(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        String value = sp.getString(key, "");
        return value;
    }

    public int getIntSavedPreferences(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        int value = sp.getInt(key, 0);
        return value;
    }

    public long getLongSavedPreferences(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        long value = sp.getLong(key, 0);
        return value;
    }

    public float getFloatSavedPreferences(String key) {
        SharedPreferences sp = mContext.getSharedPreferences(PREF_NAME,
                PRIVATE_MODE);
        float value = sp.getFloat(key, 0);
        return value;
    }
}
