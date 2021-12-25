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
    public static final String USER_PHOTO = "user_photo";

    public static void putInt(Context context, String prefName, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String prefName, String key, int defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultValue);
    }

    public static void putString(Context context, String prefName, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String prefName, String key,
                                   String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    public static void putBoolean(Context context, String prefName, String key, boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String prefName, String key,
                                     boolean defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultValue);
    }
}
