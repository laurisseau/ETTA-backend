package com.etta.edtech.controller;

import com.etta.edtech.model.User;
import com.etta.edtech.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {

        String email = user.getEmail();
        String username = user.getUsername();
        String password = user.getPassword();
        String role = user.getRole();

        return authenticationService.signUp(email, role,
                username,  password);
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        return authenticationService.login(username, password);
    }
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody User user) {
        String email = user.getEmail();
        return authenticationService.forgotPassword(email);
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody User user) {
        String email = user.getEmail();
        String newPassword = user.getResetPassword();
        String resetConfirmationCode = user.getResetConfirmationCode();
        return authenticationService.resetPassword(email, newPassword, resetConfirmationCode);
    }




}
