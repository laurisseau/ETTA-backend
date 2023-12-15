package com.etta.edtech.controller;

import com.etta.edtech.model.Course;
import com.etta.edtech.model.Enrolled;
import com.etta.edtech.model.User;
import com.etta.edtech.service.CourseService;
import com.etta.edtech.service.UserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserAuthenticationService userAuthenticationService;
    private final CourseService courseService;

    private String extractAccessToken(String authorizationHeader) {
        // Assuming a Bearer token is used, extract the token value
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            // Handle the case where the header doesn't contain a Bearer token
            throw new IllegalArgumentException("Invalid Authorization header");
        }
    }
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

    @PostMapping("/joinClass")
    public ResponseEntity<Enrolled> joinClass(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Course course) {
        String accessToken = extractAccessToken(authorizationHeader);
        String courseId = course.getCourseId();
        return courseService.joinClass(accessToken, courseId);
    }

}
