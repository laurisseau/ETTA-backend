package com.etta.edtech.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {
    private String sub;
    private String email;
    private String username;
    private String role;
    private String accessToken;
    private String password;
    private String resetPassword;
    private String resetConfirmationCode;
    private String error;
    private String studentFirstname;
    private String studentLastname;
    private String parentFirstname;
    private String parentLastname;
    private String parentPhoneNumber;
    private String parentEmailAddress;
    private String school;
    private String dateJoined;
    private String grade;
    private String age;


    public User(){

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+role));
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
}
