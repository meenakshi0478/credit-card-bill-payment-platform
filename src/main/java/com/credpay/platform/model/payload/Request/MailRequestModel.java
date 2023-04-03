package com.credpay.platform.model.payload.Request;

import lombok.Data;

@Data
public class MailRequestModel {

    private String name;
    private String to;
    private String subject;

}
