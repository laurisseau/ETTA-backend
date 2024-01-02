package com.etta.edtech.service;

import com.etta.edtech.model.Lesson;
import com.etta.edtech.repository.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {

    private final LessonRepository lessonRepository;
    public ResponseEntity<Lesson> createLesson(Lesson lesson){
        return ResponseEntity.ok(lessonRepository.save(lesson));
    }
    public ResponseEntity<Optional<Lesson>> getLessonById(Integer id){
        return ResponseEntity.ok(lessonRepository.findById(id));
    }
}
