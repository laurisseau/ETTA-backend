package com.etta.edtech.service;

import com.etta.edtech.model.Lesson;
import com.etta.edtech.model.LessonPage;
import com.etta.edtech.repository.LessonPageRepository;
import com.etta.edtech.repository.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final LessonRepository lessonRepository;
    private final LessonPageRepository lessonPageRepository;
    public ResponseEntity<Optional<Lesson>> getLessonById(Integer id){
        return ResponseEntity.ok(lessonRepository.findById(id));
    }

    public ResponseEntity<List<LessonPage>> getAllLessonPages(Integer id){
        return ResponseEntity.ok(lessonPageRepository.findAllByLessonId(id));
    }
}
