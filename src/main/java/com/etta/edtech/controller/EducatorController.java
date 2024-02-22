package com.etta.edtech.controller;

import com.etta.edtech.model.Course;
import com.etta.edtech.model.Educator;
import com.etta.edtech.model.Enrolled;
import com.etta.edtech.service.CourseService;
import com.etta.edtech.service.EducatorAuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Object> updateProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Educator educator) {
        String accessToken = extractAccessToken(authorizationHeader);
        return educatorAuthenticationService.updateProfile(accessToken, educator);
    }

    @PostMapping("/createCourse")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping("/getAllEnrolled/{educatorId}")
    public List<Enrolled> getAllEnrolled(@PathVariable String educatorId) {

        return courseService.getAllEnrolled(educatorId);
    }

    @GetMapping("/getCourseByEducatorId/{educatorId}")
    public Course getCourseById(@PathVariable String educatorId) {
        return courseService.getCourseById(educatorId);
    }

    @Transactional
    @DeleteMapping("/deleteCourse/{educatorId}")
    public void deleteCourse(@PathVariable String educatorId){
        courseService.deleteCourse(educatorId);
    }


}
