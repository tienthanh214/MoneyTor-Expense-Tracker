package com.hcmus.group14.moneytor.data.model;

import android.content.Context;

import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class UserPref {
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_PHOTO = "user_photo";
    public static final String USER_LANGUAGE = "user_language";
    public static final String USER_DARK_MODE = "user_dark_mode";
    public static final String USER_REMINDER_INTERVAL = "user_reminder_interval";

    private String name;
    private String id;
    private String email;
    private String language;
    private boolean darkMode;
    private int reminderInterval;

    public UserPref() {
    }

    public UserPref(String name, String id, String email, String language, boolean darkMode,
                    int reminderInterval) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.language = language;
        this.darkMode = darkMode;
        this.reminderInterval = reminderInterval;
    }

    public static void saveToSharedPref(Context context, UserPref userPref) {
        PreferenceUtils.putString(context, USER_NAME, userPref.name);
        PreferenceUtils.putString(context, USER_ID, userPref.id);
        PreferenceUtils.putString(context, USER_EMAIL, userPref.email);
        PreferenceUtils.putString(context, USER_LANGUAGE, userPref.language);
        PreferenceUtils.putBoolean(context, USER_DARK_MODE, userPref.darkMode);
        PreferenceUtils.putInt(context, USER_REMINDER_INTERVAL, userPref.reminderInterval);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public int getReminderInterval() {
        return reminderInterval;
    }

    public void setReminderInterval(int reminderInterval) {
        this.reminderInterval = reminderInterval;
    }
}
