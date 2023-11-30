package com.etta.edtech.controller;

import com.etta.edtech.model.User;
import com.etta.edtech.service.UserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserAuthenticationService userAuthenticationService;
    @GetMapping("/get")
    public ResponseEntity<String> simpleGet() {
        return ResponseEntity.ok("Im a user");
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<Object> updateProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody User user) {
        String accessToken = extractAccessToken(authorizationHeader);
        String email = user.getEmail();
        String username = user.getUsername();
        return userAuthenticationService.updateProfile(accessToken, email, username);
    }

    private String extractAccessToken(String authorizationHeader) {
        // Assuming a Bearer token is used, extract the token value
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            // Handle the case where the header doesn't contain a Bearer token
            throw new IllegalArgumentException("Invalid Authorization header");
        }
    }

}
