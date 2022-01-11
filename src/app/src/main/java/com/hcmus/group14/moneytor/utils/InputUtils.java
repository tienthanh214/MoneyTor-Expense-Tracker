package com.hcmus.group14.moneytor.utils;

import android.text.Editable;
import android.text.Selection;
import android.util.Log;

import java.util.Locale;

// utils for validation user's input
public class InputUtils {
    public enum Type {
        COST, CATEGORY, DEBT_LEND_TARGET
    }
    // bitmask store error
    private byte errorMask = 0;

    public void setError(Type errorType) {
        this.errorMask |= 1 << (errorType.ordinal());
    }

    public boolean isValid(Type errorType) {
        return (this.errorMask >> (errorType.ordinal()) & 1) == 0;
    }

    public boolean hasError() {
        return errorMask != 0;
    }

    // handle utf8 search
    public static String simplify(String str) {
        str = str.replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a");
        str = str.replaceAll("[èéẹẻẽêềếệểễ]", "e");
        str = str.replaceAll("[ìíịỉĩ]", "i");
        str = str.replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o");
        str = str.replaceAll("[ùúụủũưừứựửữ]", "u");
        str = str.replaceAll("[ỳýỵỷỹ]", "y");
        str = str.replaceAll("đ", "d");

        str = str.replaceAll("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]", "A");
        str = str.replaceAll("[ÈÉẸẺẼÊỀẾỆỂỄ]", "E");
        str = str.replaceAll("[ÌÍỊỈĨ]", "I");
        str = str.replaceAll("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]", "O");
        str = str.replaceAll("[ÙÚỤỦŨƯỪỨỰỬỮ]", "U");
        str = str.replaceAll("[ỲÝỴỶỸ]", "Y");
        str = str.replaceAll("Đ", "D");
        return str.toLowerCase();

    }

    public static String getCurrencyFormat(Editable s) {
        String str = s.toString();
        long value = 0L;
        if (!str.isEmpty()) {
            str = str.replace(",", "");
            if (str.length() > 13)
                str = str.substring(0, 13);
            value = Long.parseLong(str);
            Selection.setSelection(s, s.toString().length());
        }
        return String.format(Locale.US, "%,d", value);
    }

    public static String getCurrencyFormat(long value) {
        return String.format(Locale.US, "%,d", value);
    }

    public static Long getCurrencyInLong(String s) {
        return Long.parseLong(s.replace(",", ""));
    }

    public static String getCurrency(long value) {
        return getCurrencyFormat(value) + " VNĐ";
    }
}
