package com.hcmus.group14.moneytor.receiver;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.SpendGoal;

public class GoalBroadcastReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "spending_goal_channel";
    SpendGoal goal;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(context.getString(R.string.bundle_goal_obj));

        if (bundle != null)
            goal = (SpendGoal) bundle.getSerializable(context.getString(R.string.arg_goal_obj));
        String toastText = "Alarm Received";
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();

        if (goal != null) {
            notifyUser(context);
        }
    }

    private void notifyUser(Context context) {
        final int id = goal.getGoalID();

        String channelName = "Spending Goal";
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Spending goal - " + goal.getCategory())
                .setContentText(goal.getDesc())
                // TODO: replace with app icon
                //.setSmallIcon(R.drawable.wall_clock)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

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

    void setNextReminder(Context context, long milliseconds) {
    }
}
