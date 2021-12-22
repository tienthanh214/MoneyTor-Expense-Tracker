package com.hcmus.group14.moneytor.utils;

// utils for validation user's input
public class InputUtils {
    public static enum Type {
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
}
