package com.etta.edtech.service;

import com.etta.edtech.model.Course;
import com.etta.edtech.model.Enrolled;
import com.etta.edtech.model.User;
import com.etta.edtech.repository.CourseRepository;
import com.etta.edtech.repository.EnrolledRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrolledRepository enrolledRepository;
    private final UserAuthenticationService userAuthenticationService;
    public ResponseEntity<Course> createCourse(Course course){
        return ResponseEntity.ok(courseRepository.save(course));
    }

    public Course findCourse(String courseId){
        return courseRepository.findByCourseId(courseId);
    }

    public ResponseEntity<Enrolled> joinClass(String accessToken, String courseId){


        User user = userAuthenticationService.findUser(accessToken);

        String cognitoEmail = user.getEmail();
        String cognitoName = user.getUsername();
        String cognitoUserId = user.getSub();

        Course course = findCourse(courseId);

        Enrolled enrolled = new Enrolled();

        enrolled.setCourse(course);
        enrolled.setCognitoUserId(cognitoUserId);
        enrolled.setCognitoName(cognitoName);
        enrolled.setCognitoEmail(cognitoEmail);

        return ResponseEntity.ok(enrolledRepository.save(enrolled));
    }



}
