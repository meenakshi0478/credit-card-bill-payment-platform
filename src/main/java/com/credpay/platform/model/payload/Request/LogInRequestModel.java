package com.credpay.platform.model.payload.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInRequestModel {

    private String email;

    private String password;
}
