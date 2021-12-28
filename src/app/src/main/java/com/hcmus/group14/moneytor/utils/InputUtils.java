package com.hcmus.group14.moneytor.utils;

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
}
