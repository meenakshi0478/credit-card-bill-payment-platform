package com.credpay.platform.service;

import com.credpay.platform.InitialUserSetup;
import com.credpay.platform.dto.UpdateUserDto;
import com.credpay.platform.dto.UserDto;
import com.credpay.platform.exceptions.ErrorMessages;
import com.credpay.platform.exceptions.UserServiceException;
import com.credpay.platform.model.User;
import com.credpay.platform.repository.UserRepository;
import com.credpay.platform.security.UserPrincipals;
import com.credpay.platform.shared.Utils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailSenderService emailSenderService;


    public UserDto createUser(UserDto userDto) {

//        emailSenderService.sendMailWithAttachment("meenakshimanirudhan@gmail.com","test","test","/home/sayone/Downloads/SQLNotes.pdf");

        if (userRepository.findByEmail(userDto.getEmail()) !=null)
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        User user = new User();
        BeanUtils.copyProperties(userDto, user );

        String publicUserId = utils.generateUserId(30);

        user.setUserId(publicUserId);
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        User storedUserDetails = userRepository.save(user);

        emailSenderService.sendSimpleEmail("meenakshimanirudhan@gmail.com"," Registration Successfull",
                "Hi " + user.getFirstName() + " Welcome to CredPay. " +
                        " Thank you for creating Credpay account. For the next 12 months, you'll have free access to all CredPay services within the limits " +
                        " You can read and download all types of books from our store. " +
                        " Welcome to CredPay!");
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(email);
         return new UserPrincipals(user);
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
    }


    public UserDto getUser(String email) {
        User user= userRepository.findByEmail(email);
        if(user == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(user, returnValue);
        return returnValue;

    }

    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        User user= userRepository.findByUserId(userId);
        if(user== null) throw new UsernameNotFoundException(userId);
        BeanUtils.copyProperties(user,returnValue);
        return returnValue;
    }

    public UserDto updateUser(String userId, UpdateUserDto updateUserDto) {
        UserDto returnValue = new UserDto();
        User user = userRepository.findByUserId(userId);
        if(user==null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setPhoneNumber(updateUserDto.getPhoneNumber());
        User updatedUserDetails = userRepository.save(user);
        BeanUtils.copyProperties(updatedUserDetails, returnValue);
        return returnValue;
    }

    public void deleteUserById(String userId) {
        User user = userRepository.findByUserId(userId);

        if (user == null)
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(user);
        emailSenderService.sendSimpleEmail(user.getEmail()," CREDPAY Account Deactivated",
                "Hi " + user.getFirstName() + " Your CredPay Account has been Deactivated " +
                        " Regard CredPay Team");

    }



}



