package com.hcmus.group14.moneytor.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    private static final Calendar calendar = Calendar.getInstance();
    @SuppressLint("ConstantLocale")
    final private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    @SuppressLint("ConstantLocale")
    final private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    // constant
    private static final long DAILY_INTERVAL = 1000 * 24 * 60 * 60;
    public static final long WEEKLY_INTERVAL = 7 * DAILY_INTERVAL;
    public static final long MONTHLY_INTERVAL = 31 * DAILY_INTERVAL;
    public static final long ANNUALLY_INTERVAL = 366 * DAILY_INTERVAL;

    public static String getDate(long time) {
        if (time < 0)
            return DATE_FORMAT.format(getCurrentTimeMillis());
        return DATE_FORMAT.format(time);
    }

    public static String getTime(long time) {
        if (time < 0)
            return TIME_FORMAT.format(getCurrentTimeMillis());
        return TIME_FORMAT.format(time);
    }

    public static long getMillisByDate(int day, int month, int year) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return calendar.getTimeInMillis();
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long getDateInMillis(String date) {
        try {
            Date d = DATE_FORMAT.parse(date);
            return d != null ? d.getTime() : getCurrentTimeMillis();
        } catch (Exception e) {
            return getCurrentTimeMillis();
        }
    }
    public static String changeFormatToJapanese(String date)
    {
        assert date.matches("^[0-9]{2}\\/[0-9]{2}\\/[0-9]{4}$"); //Must match the exact format of dd/MM/yyyy
        String[] split = date.split("\\/");
        return split[2] + "年" + split[1] + "月" + split[0] + "日"; //ex: 31/12/2021 ->　2021年12月31日
    }
}
