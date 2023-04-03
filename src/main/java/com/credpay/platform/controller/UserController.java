package com.credpay.platform.controller;

import com.credpay.platform.dto.UpdateUserDto;
import com.credpay.platform.dto.UserDto;
import com.credpay.platform.model.payload.Response.UserRestModel;
import com.credpay.platform.repository.UserRepository;
import com.credpay.platform.service.UserService;
import com.credpay.platform.shared.ApiResponse;
import com.credpay.platform.shared.CustomCredPayHttpStatus;
import io.sentry.Sentry;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")

public class UserController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    public void User() {
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable String id ) {
        Map data = new HashMap();
        try {
            UserRestModel userRestModel = userService.getUserByUserId(id);
            data.put("data", userRestModel);
            return ResponseEntity.ok(new ApiResponse(data, "success", CustomCredPayHttpStatus.SUCCESS.ordinal()));
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(data, "failed", CustomCredPayHttpStatus.FAILED.ordinal()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserDto userDto) {
        Map data = new HashMap();
        try {
            if (userDto.getFirstName().isEmpty()) throw new RuntimeException ("first name needed");
            userDto.setRoles(Arrays.asList("ROLE_USER"));
            UserRestModel createdUser = userService.createUser(userDto);
            data.put("data", createdUser);
            return ResponseEntity.ok(new ApiResponse(data, "success", CustomCredPayHttpStatus.SUCCESS.ordinal()));
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(data, "failed", CustomCredPayHttpStatus.FAILED.ordinal()));
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable String id, @RequestBody UpdateUserDto updateUserDto) {
        Map data = new HashMap();
        try {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(updateUserDto, userDto);
            UserDto updateUser = userService.updateUser(id, updateUserDto);
            data.put("data", updateUser);
            return ResponseEntity.ok(new ApiResponse(data, "success", CustomCredPayHttpStatus.SUCCESS.ordinal()));
        }catch (Exception ex) {
            Sentry.captureException(ex);
            data.put("message", ex.getClass().getName() + ": " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(data, "failed", CustomCredPayHttpStatus.FAILED.ordinal()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity deleteUserById(@PathVariable String id) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDto user = userService.getUser(auth.getName());
            userService.deleteUserById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Account has been deactivated");
        } catch (Exception ex) {
            Sentry.captureException(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not deactivate the account");
        }
    }
}
