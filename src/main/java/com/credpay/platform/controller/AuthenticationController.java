package com.credpay.platform.controller;

import com.credpay.platform.model.payload.Request.LogInRequestModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @ApiOperation(value = "login")
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Response Headers",
                    responseHeaders = {
                            @ResponseHeader(name = "authorization",
                                    description = "Bearer <JWT value here>")
                    })
    })

    @PostMapping("users/login")
    public void theFakeLogin(@RequestBody LogInRequestModel logInRequestModel){
        throw new IllegalStateException("this method is should not be called. This is implemented by spring security by spring security");
    }
}
