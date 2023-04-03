package com.credpay.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private static final long serialVersionUID = 6473858950438988814L;
    private Long Id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private Collection <String> roles;

}
