package com.hcmus.group14.moneytor.utils;

import static com.hcmus.group14.moneytor.services.notification.receiver.NoteBroadcastReceiver.NOTE_REQUEST_CODE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.services.notification.receiver.GoalBroadcastReceiver;
import com.hcmus.group14.moneytor.services.notification.receiver.NoteBroadcastReceiver;

import java.util.Calendar;

public class NotificationUtils {

    public static void scheduleGoalNotif(Context context, SpendGoal goal) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GoalBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_goal_obj), goal);
        intent.putExtra(context.getString(R.string.bundle_goal_obj), bundle);
        // Create a pending intent that will be called later
        PendingIntent notifPendingIntent = PendingIntent.getBroadcast(
                context,
                goal.getGoalID(),
                intent,
                0);
        // Get the time that notification will be evoke from the set time
        // TODO: change time here for demoing
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(goal.getDate());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        // Set the notification
        try {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    notifPendingIntent);

            String toastText = String.format(
                    "Spending goal reminder set %d scheduled at %d:%d:%d",
                    goal.getGoalID(),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND));
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelGoalNotif(Context context, SpendGoal goal) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GoalBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(
                context,
                goal.getGoalID(),
                intent,
                0);
        alarmManager.cancel(alarmPendingIntent);
    }

    public static void scheduleNoteReminder(Context context, int hour, int minute, int interval) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NoteBroadcastReceiver.class);
        // Create a pending intent that will be called later
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(
                context,
                NOTE_REQUEST_CODE,
                intent,
                0);
        // Get time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        // Interval of one day
        if (interval < 1) interval = 1;
        final long intervalInMillis = 24L * 60 * 60 * 1000 * interval;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                intervalInMillis,
                alarmPendingIntent);

        String toastText = String.format(
                "Reminder scheduled for every %d days at %d:%d",
                interval,
                hour,
                minute);

        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

    public static void cancelNoteReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NoteBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(
                context,
                NOTE_REQUEST_CODE,
                intent,
                0);
        alarmManager.cancel(alarmPendingIntent);
    }
}
