package com.credpay.platform.service;

import com.credpay.platform.dto.UpdateUserDto;
import com.credpay.platform.dto.UserDto;
import com.credpay.platform.exceptions.ErrorMessages;
import com.credpay.platform.exceptions.UserServiceException;
import com.credpay.platform.model.BankAccount;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.model.Role;
import com.credpay.platform.model.User;
import com.credpay.platform.model.payload.Request.MailRequestModel;
import com.credpay.platform.model.payload.Response.UserRestModel;
import com.credpay.platform.repository.BankAccountRepository;
import com.credpay.platform.repository.CreditCardRepository;
import com.credpay.platform.repository.RoleRepository;
import com.credpay.platform.repository.UserRepository;
import com.credpay.platform.security.UserPrincipal;
import com.credpay.platform.shared.Utils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils utils;


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;


    public UserRestModel createUser(UserDto userDto) {


        if (userRepository.findByEmail(userDto.getEmail()) !=null)
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        User user = new User();
        Collection<Role>roleEntities =  new HashSet<>();
        for(String role: userDto.getRoles()){
           Role roleEntity = roleRepository.findByName(role);
           if (roleEntity != null) {
               roleEntities.add(roleEntity);
           }
        }
        BeanUtils.copyProperties(userDto, user );

        String publicUserId = utils.generateUserId(30);

        user.setUserId(publicUserId);
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setRoles(roleEntities);

        User storedUserDetails = userRepository.save(user);

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getFirstName());
        MailRequestModel request = new MailRequestModel();
        request.setTo(userDto.getEmail());
        request.setName(userDto.getFirstName());
        request.setSubject("Registration Successfull");
        emailSenderService.sendEmail(request, model);
        UserRestModel userRestModel = new UserRestModel();
        BeanUtils.copyProperties(storedUserDetails, userRestModel);
        return userRestModel;
    }



    public UserDto getUser(String email) {
        User user= userRepository.findByEmail(email);
        if(user == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(user, returnValue);
        return returnValue;

    }

    public UserRestModel getUserByUserId(String userId) {
        UserRestModel returnValue = new UserRestModel();
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
        CreditCard creditCard = creditCardRepository.findByUserId(userId);
        if(creditCard!=null)
        creditCardRepository.delete(creditCard);
        BankAccount bankAccount = bankAccountRepository.findByUserId(userId);
        if(bankAccount!=null)
        bankAccountRepository.delete(bankAccount);

        emailSenderService.sendSimpleEmail(user.getEmail()," CREDPAY Account Deactivated",
                "Hi " + user.getFirstName() + " Your CredPay Account has been Deactivated " +
                        " Regard CredPay Team");

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException(email);
        return new UserPrincipal(user);
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPassword(), new ArrayList<>());
    }



}



