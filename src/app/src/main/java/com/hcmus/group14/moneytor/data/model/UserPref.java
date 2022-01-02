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
    public static final String USER_WIDGET = "user_widget";
    public static final String USER_REMINDER = "user_reminder";

    private String name;
    private String id;
    private String email;
    private String language;
    private String darkMode;
    private int reminderInterval;
    private boolean widget;
    private int reminder;

    public UserPref() {
    }

    public UserPref(String name, String id, String email, String language, String darkMode,
                    int reminderInterval, boolean widget, int reminder) {
        this.name = name;
        this.id = id;
        this.email = email;
        this.language = language;
        this.darkMode = darkMode;
        this.reminderInterval = reminderInterval;
        this.widget = widget;
        this.reminder = reminder;
    }

    public static void saveToSharedPref(Context context, UserPref userPref) {
        PreferenceUtils.putString(context, USER_NAME, userPref.name);
        PreferenceUtils.putString(context, USER_ID, userPref.id);
        PreferenceUtils.putString(context, USER_EMAIL, userPref.email);
        PreferenceUtils.putString(context, USER_LANGUAGE, userPref.language);
        PreferenceUtils.putString(context, USER_DARK_MODE, userPref.darkMode);
        // TODO: check if reminder interval is needed
        PreferenceUtils.putInt(context, USER_REMINDER_INTERVAL, userPref.reminderInterval);
        PreferenceUtils.putBoolean(context, USER_WIDGET, userPref.widget);
        PreferenceUtils.putInt(context, USER_REMINDER, userPref.reminder);
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

    public String isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(String darkMode) {
        this.darkMode = darkMode;
    }

    public int getReminderInterval() {
        return reminderInterval;
    }

    public void setReminderInterval(int reminderInterval) {
        this.reminderInterval = reminderInterval;
    }

    public boolean isWidget() {
        return widget;
    }

    public void setWidget(boolean widget) {
        this.widget = widget;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }
}
