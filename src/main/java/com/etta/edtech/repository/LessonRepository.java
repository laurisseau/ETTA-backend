package com.etta.edtech.repository;

import com.etta.edtech.model.Enrolled;
import com.etta.edtech.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
}
