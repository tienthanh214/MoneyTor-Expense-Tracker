package com.hcmus.group14.moneytor.firebase;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;
// TODO: testing only, will apply MVVM latter
public class UserProfile {
    static public void importUserProfile(Context context, FirebaseUser user) {
        PreferenceUtils.putString(
                PreferenceUtils.getInstance(
                        context,
                        context.getString(R.string.user_profile)),
                context.getString(R.string.user_name),
                user.getDisplayName());
    }
}
