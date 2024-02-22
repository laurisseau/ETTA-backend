package com.etta.edtech.service;

import com.etta.edtech.exceptions.ClassExceptions;
import com.etta.edtech.model.Course;
import com.etta.edtech.model.Enrolled;
import com.etta.edtech.model.User;
import com.etta.edtech.repository.CourseRepository;
import com.etta.edtech.repository.EnrolledRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    public Boolean isEnrolled(String cognitoUserId){
        return enrolledRepository.existsByCognitoUserId(cognitoUserId);
    }
    public ResponseEntity<String> joinClass(String accessToken, String courseId){

        User user = userAuthenticationService.findUser(accessToken);

        String cognitoEmail = user.getEmail();
        String cognitoName = user.getUsername();
        String cognitoUserId = user.getSub();

        if(isEnrolled(cognitoUserId)){
            throw new ClassExceptions("Already Enrolled in a class");
        }

        Course course = findCourse(courseId);

        if (course == null) {
            throw new ClassExceptions("Wrong class code");
        }

        Enrolled enrolled = new Enrolled();

        enrolled.setCourse(course);
        enrolled.setCognitoUserId(cognitoUserId);
        enrolled.setCognitoName(cognitoName);
        enrolled.setCognitoEmail(cognitoEmail);

        enrolledRepository.save(enrolled);
        return ResponseEntity.ok("You have joined a class click on the course you would like to learn about.");

    }

    public List<Enrolled> getAllEnrolled(String educatorId){
        return enrolledRepository.findAllByCourse_EducatorId(educatorId);
    }

    public Course getCourseById(String educatorId){
        return courseRepository.findByEducatorId(educatorId);
    }

    public void deleteCourse(String educatorId){
        enrolledRepository.deleteAllByCourse_EducatorId(educatorId);
        courseRepository.deleteByEducatorId(educatorId);
    }


}
