package com.credpay.platform.exceptions;

public class UserServiceException extends RuntimeException{
    public UserServiceException(String message) {
        super(String.valueOf(message));
    }
}