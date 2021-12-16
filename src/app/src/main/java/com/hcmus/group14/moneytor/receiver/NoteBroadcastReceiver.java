package com.hcmus.group14.moneytor.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

import com.hcmus.group14.moneytor.R;

public class NoteBroadcastReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "note_reminder_channel";
    public static final int notifId = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String channelName = "Reminder";
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.reminder_title))
                .setContentText(context.getString(R.string.reminder_message))
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
        notificationManager.notify(notifId, builder.build());
    }

    void setNextReminder(Context context) {

    }
}
