package com.hcmus.group14.moneytor.data.model;

import android.content.Context;

import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class UserPref {
    private String name;
    private String id;
    private String email;

    public UserPref() {
    }

    public UserPref(String name, String id, String email) {
        this.name = name;
        this.id = id;
        this.email = email;
    }

    public static void saveToSharedPref(Context context, UserPref userPref) {
        PreferenceUtils.putString(context,
                PreferenceUtils.USER_PROFILE,
                PreferenceUtils.USER_NAME,
                userPref.name);
        PreferenceUtils.putString(context,
                PreferenceUtils.USER_PROFILE,
                PreferenceUtils.USER_ID,
                userPref.id);
        PreferenceUtils.putString(context,
                PreferenceUtils.USER_PROFILE,
                PreferenceUtils.USER_EMAIL,
                userPref.email);
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
}
