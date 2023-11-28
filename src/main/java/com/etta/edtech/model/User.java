package com.etta.edtech.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class User {
    private String email;
    private String username;
    private String Role;
    private String password;
    private String resetPassword;
    private String resetConfirmationCode;
}
