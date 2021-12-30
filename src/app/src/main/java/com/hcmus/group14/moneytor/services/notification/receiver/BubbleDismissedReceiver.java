package com.hcmus.group14.moneytor.services.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hcmus.group14.moneytor.data.model.UserPref;
import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class BubbleDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceUtils.putBoolean(context, UserPref.USER_WIDGET, false);
    }
}
