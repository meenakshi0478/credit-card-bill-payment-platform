package com.credpay.platform.shared;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class ApiResponse {

    private final Map responseData;
    private final String message;
    private final CustomCredPayHttpStatus statusTag = null;
    private final Integer status;
    private List<String> detailedMessages;


    public ApiResponse(Map responseData, List<String> detailedMessages, String message, Integer status) {
        this.responseData = responseData;
        this.message = message;
        this.status = status;
        this.detailedMessages = detailedMessages;
    }

    public ApiResponse(Map responseData, String message, Integer status) {
        this.responseData = responseData;
        this.message = message;
        this.status = status;
    }

    public Map getResponseData() {
        return responseData;
    }

    public List<String> getDetailedMessages() {
        return detailedMessages;
    }

    public void setDetailedMessages(List<String> detailedMessages) {
        this.detailedMessages = detailedMessages;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }
}
