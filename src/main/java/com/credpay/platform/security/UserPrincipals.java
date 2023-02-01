package com.credpay.platform.security;

import com.credpay.platform.model.Authority;
import com.credpay.platform.model.Role;
import com.credpay.platform.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipals implements UserDetails {

    private static final long serialVersionUID= -3206518490338399368L;

    private User user;
    private String userId;

    public UserPrincipals(User user) {

        this.user = user;
        this.userId = user.getUserId();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();
        List<Authority> authorityEntities = new ArrayList<>();

        //Get user roles
        Collection<Role>roles = user.getRoles();
        if(roles==null) return authorities;

        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorityEntities.addAll(role.getAuthorities());
        });

        authorityEntities.forEach(authority ->{
            authorities.add(new SimpleGrantedAuthority(authority.getName()));
        } );
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.user.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
