package com.credpay.platform.util;

public enum CustomHttpStatus {
    SUCCESS(0),
    FAILED(1);


    private final int status;

    CustomHttpStatus(int status) {
        this.status = status;
    }

    public int asMonthOfYear() {
        return status;
    }
}
