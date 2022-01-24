package com.hcmus.group14.moneytor.services.notification.receiver;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.ui.spending.AddSpendingActivity;
import com.hcmus.group14.moneytor.ui.widget.BubbleActivity;

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

    public void setupWidget(Context context, SpendGoal goal) {
        this.goal = goal;
        notifyUser(context);
    }

    private void notifyUser(Context context) {
        String channelName = "Spending Goal";
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
            if (goal.getGoalID() == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                notificationChannel.setAllowBubbles(true);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Spending goal - " + goal.getCategory())
                .setContentText(goal.getDesc())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        // TODO: bubble WIDGET
        if (goal.getGoalID() == 0) {
            Intent target = new Intent(context, BubbleActivity.class);
            PendingIntent bubbleIntent = PendingIntent.getActivity(context, 0, target, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.BubbleMetadata bubbleData = new NotificationCompat.BubbleMetadata.Builder(
                    bubbleIntent, IconCompat.createWithResource(context, R.drawable.ic_app_logo))
                    .setDesiredHeight(600)
                    .setAutoExpandBubble(false)
                    .setSuppressNotification(true)
                    .setDeleteIntent(createOnDismissedIntent(context))
                    .build();
            builder.setBubbleMetadata(bubbleData);
            builder.setContentTitle("Widget");
            builder.setContentText(context.getString(R.string.bubble_notif));
        }

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
        // when delete notification
        notificationManager.notify(goal.getGoalID(), builder.build());
    }

    private PendingIntent createOnDismissedIntent(Context context) {
        Intent target = new Intent(context, BubbleDismissedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, target, PendingIntent.FLAG_ONE_SHOT);

        return pendingIntent;
    }
}
