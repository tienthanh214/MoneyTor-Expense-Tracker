package com.hcmus.group14.moneytor.services.notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hcmus.group14.moneytor.utils.PreferenceUtils;

public class BubbleDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("@@@ alo", "inside me");
        PreferenceUtils.putBoolean(context, "homescreen_widget", false);
    }
}
