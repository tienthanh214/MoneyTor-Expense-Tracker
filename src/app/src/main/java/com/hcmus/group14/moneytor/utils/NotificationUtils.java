package com.hcmus.group14.moneytor.utils;

import static com.hcmus.group14.moneytor.receiver.NoteBroadcastReceiver.notifId;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.hcmus.group14.moneytor.R;
import com.hcmus.group14.moneytor.data.model.SpendGoal;
import com.hcmus.group14.moneytor.receiver.GoalBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NotificationUtils {
    private static final Calendar calendar = Calendar.getInstance();
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.getDefault());

//    public static String getHourMinute(long time) {
//        return TIME_FORMAT.format(time);
//    }

    public static long getTimeMillis(int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }

//    public static String getDaysInWeek(int mask) {
//        StringBuilder result = new StringBuilder();
//        for (WeekDays w : WeekDays.values()) {
//            int x = w.ordinal();
//            if ((mask >> x & 1) == 1) {
//                result.append(w.toString());
//                result.append(" ");
//            }
//        }
//        return result.toString();
//    }
//
//    public static byte getBitFormat(WeekDays... days) {
//        byte result = 0;
//        for (WeekDays w : days) {
//            int x = w.ordinal();
//            result |= 1 << x;
//        }
//        return result;
//    }
//
//    public static byte getBitFormat(boolean... days) {
//        byte result = 0;
//        for (int i = 0; i < 7; ++i) {
//            result |= (days[i] ? 1 : 0) << i;
//        }
//        return result;
//    }

//    public static String toWeekDay(int day) throws Exception {
//        // Get week day name in String
//        switch (day) {
//            case Calendar.SUNDAY:
//                return "Sunday";
//            case Calendar.MONDAY:
//                return "Monday";
//            case Calendar.TUESDAY:
//                return "Tuesday";
//            case Calendar.WEDNESDAY:
//                return "Wednesday";
//            case Calendar.THURSDAY:
//                return "Thursday";
//            case Calendar.FRIDAY:
//                return "Friday";
//            case Calendar.SATURDAY:
//                return "Saturday";
//        }
//        throw new Exception("Could not locate day");
//    }

    public static void scheduleGoalNotif(Context context, SpendGoal goal) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, GoalBroadcastReceiver.class);
        // Put extra info that comes with the notification
        Bundle bundle = new Bundle();
        bundle.putSerializable(context.getString(R.string.arg_goal_obj), goal);
        intent.putExtra(context.getString(R.string.bundle_goal_obj), bundle);
        // Create a pending intent that will be called later
        PendingIntent notifPendingIntent = PendingIntent.getBroadcast(context,
                goal.getGoalID(),
                intent, 0);
        // Get the time that notification will be evoke from the set time
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.setTimeInMillis(goal.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, setCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, setCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // if alarm time has already passed, increment day by 1
        if (goal.getDate() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        // Choose whether set notification everyday or one-time
        //if (!isRepeating) {
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                notifPendingIntent);
//        } else {
//            // Interval of one day
//            final long RUN_DAILY = 24 * 60 * 60 * 1000;
//            alarmManager.setRepeating(
//                    AlarmManager.RTC_WAKEUP,
//                    calendar.getTimeInMillis(),
//                    RUN_DAILY,
//                    alarmPendingIntent);
//        }
        String toastText = String.format("Alarm %d scheduled", goal.getGoalID());
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

    public static void cancelGoalNotif(Context context, SpendGoal goal) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GoalBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,
                goal.getGoalID(),
                intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        // Show toast
        String toastText = String.format("Alarm %d cancelled", goal.getGoalID());
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }

    public static void scheduleNoteReminder(Context context, int intervalInDays) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, GoalBroadcastReceiver.class);
        // Create a pending intent that will be called later
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context,
                notifId,
                intent, 0);
        // Get time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }
        // Choose whether set notification everyday or one-time
        //if (!isRepeating) {
//        alarmManager.setExact(
//                AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),
//                alarmPendingIntent);
//        } else {
        // Interval of one day
        if (intervalInDays < 1) intervalInDays = 1;
        final long interval = 24L * 60 * 60 * 1000 * intervalInDays;
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                interval,
                alarmPendingIntent);
//        }
        String toastText = String.format("Reminder scheduled for every %d days", intervalInDays);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
    }
}
