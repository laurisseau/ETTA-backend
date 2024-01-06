package com.etta.edtech.controller;

import com.etta.edtech.model.*;
import com.etta.edtech.repository.EnrolledRepository;
import com.etta.edtech.service.CourseService;
import com.etta.edtech.service.UserAuthenticationService;
import com.etta.edtech.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserAuthenticationService userAuthenticationService;
    private final UserService userService;
    private final EnrolledRepository enrolledRepository;
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
    public ResponseEntity<String> joinClass(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody Course course) {
        String accessToken = extractAccessToken(authorizationHeader);
        String courseId = course.getCourseId();
        return courseService.joinClass(accessToken, courseId);
    }

    @GetMapping("/ifEnrolled/{cognitoUserId}")
    public Enrolled ifEnrolled(@PathVariable String cognitoUserId) {
        return enrolledRepository.findByCognitoUserId(cognitoUserId);
    }

    @Transactional
    @DeleteMapping("/deleteEnrolled/{cognitoUserId}")
    public void deleteEnrolled(@PathVariable String cognitoUserId) {
        enrolledRepository.deleteByCognitoUserId(cognitoUserId);
    }

    @GetMapping("/lesson/{id}")
    public ResponseEntity<Optional<Lesson>> getLessonById(@PathVariable Integer id) {
        return userService.getLessonById(id);
    }

    @GetMapping("/lessonPages/{id}")
    public ResponseEntity<List<LessonPage>> getAllLessonPages(@PathVariable Integer id) {
        return userService.getAllLessonPages(id);
    }



}
