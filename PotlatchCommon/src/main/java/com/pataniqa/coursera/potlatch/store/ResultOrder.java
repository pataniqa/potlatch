package com.pataniqa.coursera.potlatch.store;

public enum ResultOrder {
    TIME(0), LIKES(1);

    private final int val;

    private ResultOrder(int v) {
        val = v;
    }

    public int getVal() {
        return val;
    }
    
    public static ResultOrder toEnum(int v) {
        return ResultOrder.values()[v];
    }
};
