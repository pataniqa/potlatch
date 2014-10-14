package com.pataniqa.coursera.potlatch.store;

public enum ResultOrderDirection {
    ASCENDING(0), DESCENDING(1);

    private final int val;

    private ResultOrderDirection(int v) {
        val = v;
    }

    public int getVal() {
        return val;
    }

    public static ResultOrderDirection toEnum(int v) {
        return ResultOrderDirection.values()[v];
    }
};