package com.hcmus.group14.moneytor.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
    //    <string name="user_profile">user_profile</string>
//    <string name="user_photo">user_photo</string>
//    <string name="user_name">user_name</string>
    public static final String USER_PROFILE = "user_profile";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "user_email";

    public static SharedPreferences getInstance(Context context, String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static void putInt(SharedPreferences sharedPref, String key, int value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(SharedPreferences sharedPref, String key, int defaultValue) {
        return sharedPref.getInt(key, defaultValue);
    }

    public static void putString(SharedPreferences sharedPref, String key, String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(SharedPreferences sharedPref, String key, String defaultValue) {
        return sharedPref.getString(key, defaultValue);
    }

    public static void putBoolean(SharedPreferences sharedPref, String key, boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(SharedPreferences sharedPref, String key,
                                     boolean defaultValue) {
        return sharedPref.getBoolean(key, defaultValue);
    }
}
