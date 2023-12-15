package com.etta.edtech.repository;
import com.etta.edtech.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    Course findByCourseId(String courseId);
}
