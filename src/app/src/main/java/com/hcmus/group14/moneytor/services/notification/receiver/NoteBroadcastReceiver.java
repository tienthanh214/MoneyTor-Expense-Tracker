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

import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.ui.widget.BubbleActivity;

public class NoteBroadcastReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "note_reminder_channel";
    public static final int NOTE_REQUEST_CODE = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String channelName = "Reminder";
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                notificationChannel.setAllowBubbles(true);
            }
        }
        // TODO: bubble WIDGET
        Intent target = new Intent(context, BubbleActivity.class);
        PendingIntent bubbleIntent = PendingIntent.getActivity(context, 0, target, 0);
        NotificationCompat.BubbleMetadata bubbleData =
                new NotificationCompat.BubbleMetadata.Builder(
                        bubbleIntent, IconCompat.createWithResource(context,
                        R.drawable.ic_app_logo))
                        .setDesiredHeight(700)
                        .setAutoExpandBubble(false)
                        .setSuppressNotification(true)
                        .build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.reminder_title))
                .setContentText(context.getString(R.string.reminder_message))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setBubbleMetadata(bubbleData);

        // TODO set what happen when press notification
//                Intent notificationIntent = new Intent(Intent.ACTION_VIEW,
//                        Uri.parse(alarm.getTagUri()));
//                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                        notificationIntent, 0);
//                builder.setContentText(alarm.getDescription() + " - Tap to open url: " + alarm
//                .getTagUri());
//                builder.setContentIntent(contentIntent);


        notificationManager.notify(NOTE_REQUEST_CODE, builder.build());
    }
}
