package com.credpay.platform.model.payload.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailResponseModel {
    private String message;
    private boolean status;
}
