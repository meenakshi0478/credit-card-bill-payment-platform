package com.credpay.platform.exceptions;

import com.credpay.platform.shared.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class MyControllerAdvice {
    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<String>handleUserException(UserServiceException userServiceException){
        return new ResponseEntity<String>("Exception Occured",null);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public ErrorResponse handleNullPointerException(NullPointerException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }


}

