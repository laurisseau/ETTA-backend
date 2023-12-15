package com.etta.edtech.controller;

import com.etta.edtech.model.Course;
import com.etta.edtech.model.User;
import com.etta.edtech.service.CourseService;
import com.etta.edtech.service.EducatorAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/educator")
@RequiredArgsConstructor
public class EducatorController {

    private final EducatorAuthenticationService educatorAuthenticationService;
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
    @PostMapping("/updateProfile")
    public ResponseEntity<Object> updateProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody User user) {
        String accessToken = extractAccessToken(authorizationHeader);
        String email = user.getEmail();
        String username = user.getUsername();
        return educatorAuthenticationService.updateProfile(accessToken, email, username);
    }

    @PostMapping("/createCourse")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }


}
