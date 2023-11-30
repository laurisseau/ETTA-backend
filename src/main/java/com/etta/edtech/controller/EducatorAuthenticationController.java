package com.etta.edtech.controller;

import com.etta.edtech.model.Educator;
import com.etta.edtech.model.User;
import com.etta.edtech.service.EducatorAuthenticationService;
import com.etta.edtech.service.UserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/educator")
@RequiredArgsConstructor
public class EducatorAuthenticationController {
    private final EducatorAuthenticationService educatorAuthenticationService;
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody Educator educator) {

        String email = educator.getEmail();
        String username = educator.getUsername();
        String password = educator.getPassword();
        String role = "EDUCATOR";

        return educatorAuthenticationService.signUp(email, role,
                username,  password);
    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Educator educator) {
        String username = educator.getUsername();
        String password = educator.getPassword();
        return educatorAuthenticationService.login(username, password);
    }


    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Educator educator) {
        String email = educator.getEmail();
        return educatorAuthenticationService.forgotPassword(email);
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Educator educator) {
        String email = educator.getEmail();
        String newPassword = educator.getResetPassword();
        String resetConfirmationCode = educator.getResetConfirmationCode();
        return educatorAuthenticationService.resetPassword(email, newPassword, resetConfirmationCode);
    }

}
