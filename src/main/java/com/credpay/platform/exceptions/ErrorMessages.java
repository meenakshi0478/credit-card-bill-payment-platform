package com.credpay.platform.exceptions;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing Required field. Please check documentation for required fields"),
    NO_RECORD_FOUND("No record found."),
    RECORD_ALREADY_EXISTS("Record already exists"),
    CARD_ALREADY_EXISTS("Card Already Exists"),
    BILL_ALREADY_EXISTS("Bill Already Exists");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
