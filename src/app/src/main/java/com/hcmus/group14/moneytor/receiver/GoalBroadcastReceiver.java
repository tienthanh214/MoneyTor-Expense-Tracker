package com.hcmus.group14.moneytor.receiver;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.SpendGoal;

public class GoalBroadcastReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "spending_goal_channel";
    SpendGoal goal;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(context.getString(R.string.bundle_goal_obj));
        if (bundle != null) {
            goal = (SpendGoal) bundle.getSerializable(context.getString(R.string.arg_goal_obj));
            notifyUser(context);
        }
    }

    private void notifyUser(Context context) {
        String channelName = "Spending Goal";
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Spending goal - " + goal.getCategory())
                .setContentText(goal.getDesc())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        // TODO set what happen when press notification
//        if (alarm.getTagUri() != null) {
//            if (alarm.getTagUri().length() != 0) {
//                Intent notificationIntent = new Intent(Intent.ACTION_VIEW,
//                        Uri.parse(alarm.getTagUri()));
//                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                        notificationIntent, 0);
//                builder.setContentText(alarm.getDescription() + " - Tap to open url: " + alarm
//                .getTagUri());
//                builder.setContentIntent(contentIntent);
//            }
//        }

        notificationManager.notify(goal.getGoalID(), builder.build());
    }
}
