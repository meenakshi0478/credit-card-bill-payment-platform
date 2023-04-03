package com.credpay.platform.shared;

public enum CustomCredPayHttpStatus {
    SUCCESS(0),
    FAILED(1);

    private final int status;

    CustomCredPayHttpStatus(int status) {
        this.status = status;
    }

    public int asMonthOfYear() {
        return status;
    }
}
