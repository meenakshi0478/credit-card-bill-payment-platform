package com.credpay.platform.controller;

import com.credpay.platform.dto.UpdateUserDto;
import com.credpay.platform.dto.UserDto;
import com.credpay.platform.model.payload.UserDetailsRequestModel;
import com.credpay.platform.model.payload.UserRestModel;
import com.credpay.platform.repository.UserRepository;
import com.credpay.platform.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")

public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }
    public void User() {
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
    @GetMapping("/get/{id}")
    public UserRestModel getUser(@PathVariable String id ) {

        UserRestModel returnValue = new UserRestModel();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);

        return returnValue;

    }

    @PostMapping("/signup")
    public UserRestModel createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRestModel returnValue = new UserRestModel();

        if (userDetails.getFirstName().isEmpty())
            throw new RuntimeException ("first name needed");


        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
    @PutMapping("/{id}/update")
    public UserRestModel updateUser(@PathVariable String id, @RequestBody UpdateUserDto updateUserDto) {

        UserRestModel returnValue = new UserRestModel();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(updateUserDto, userDto);

        UserDto updateUser = userService.updateUser(id, updateUserDto);
        BeanUtils.copyProperties(updateUser, returnValue);
        return returnValue;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
    @DeleteMapping("/{id}/delete")
    public String deleteUserById(@PathVariable String id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        userService.deleteUserById(id);
        return "User Deleted Successfully";
    }

}
