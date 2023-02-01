package com.credpay.platform;

import com.credpay.platform.model.Authority;
import com.credpay.platform.model.Role;
import com.credpay.platform.model.User;
import com.credpay.platform.repository.AuthorityRepository;
import com.credpay.platform.repository.RoleRepository;
import com.credpay.platform.repository.UserRepository;
import com.credpay.platform.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
public class InitialUserSetup {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){
        System.out.println("From Application Ready Event.............");
        Authority readAuthority =  createAuthority("READ_AUTHORITY");
        Authority writeAuthority =  createAuthority("WRITE_AUTHORITY");
        Authority deleteAuthority =  createAuthority("DELETE_AUTHORITY");

        Role roleUser = createRole("ROLE_USER", Arrays.asList(readAuthority,writeAuthority));
        Role roleAdmin = createRole("ROLE_ADMIN", Arrays.asList(readAuthority,writeAuthority,deleteAuthority));

        if(roleAdmin==null){ return; }
       /* User adminUser = new User();
        adminUser.setFirstName("Meenakshi");
        adminUser.setLastName("M");
        adminUser.setEmail("admin");
        adminUser.setUserId(utils.generateUserId(30));
        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("123"));
        adminUser.setRoles(Arrays.asList(roleAdmin));
        adminUser.setPhoneNumber("9744777777");

        userRepository.save(adminUser);*/
    }

    @Transactional
    private Authority createAuthority(String name){
        Authority authority= authorityRepository.findByName(name);
        if(authority==null){
            authority=new Authority(name);
            authorityRepository.save(authority);
        }
        return authority;
    }

    @Transactional
    private Role createRole(String name, Collection<Authority>authorities){
        Role role= roleRepository.findByName(name);
        if(role==null) {
            role = new Role(name);
            role.setAuthorities(authorities);
            roleRepository.save(role);
        }
        return role;
    }
}
