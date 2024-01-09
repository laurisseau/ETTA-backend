package com.etta.edtech.repository;

import com.etta.edtech.model.Course;
import com.etta.edtech.model.Enrolled;
import com.etta.edtech.model.Lesson;
import com.etta.edtech.model.LessonPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonPageRepository extends JpaRepository<LessonPage, Integer> {
    List<LessonPage> findAllByLessonId(int lessonId);
    LessonPage findByPageNum(int pageNum);
}
