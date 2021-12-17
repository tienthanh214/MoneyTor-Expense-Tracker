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

    public static String getDate(long time) {
        return DATE_FORMAT.format(time);
    }

    public static long getDateInMillis(int day, int month, int year) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
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
}
