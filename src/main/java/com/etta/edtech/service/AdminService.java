package com.etta.edtech.service;

import com.amazonaws.services.accessanalyzer.model.ResourceNotFoundException;
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
public class AdminService {

    private final LessonRepository lessonRepository;
    private final LessonPageRepository lessonPageRepository;
    public ResponseEntity<Lesson> createLesson(Lesson lesson){
        return ResponseEntity.ok(lessonRepository.save(lesson));
    }
    public ResponseEntity<Optional<Lesson>> getLessonById(Integer id){
        return ResponseEntity.ok(lessonRepository.findById(id));
    }

    public ResponseEntity<Lesson> updateLessonById(Integer id, Lesson updatedLesson){
        Lesson existingLesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));

        existingLesson.setName(updatedLesson.getName());
        existingLesson.setSlug(updatedLesson.getSlug());
        existingLesson.setLanguage(updatedLesson.getLanguage());
        existingLesson.setSubscription(updatedLesson.getSubscription());
        existingLesson.setSubscriptionId(updatedLesson.getSubscriptionId());
        existingLesson.setDescription(updatedLesson.getDescription());

        Lesson savedLesson = lessonRepository.save(existingLesson);
        return ResponseEntity.ok(savedLesson);
    }

    public String deleteLessonById(Integer id){
        lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));

        lessonRepository.deleteById(id);

        return "deleted";
    }

    public  ResponseEntity<LessonPage> addPage(LessonPage lessonPage){
        return ResponseEntity.ok(lessonPageRepository.save(lessonPage));
    }

    public ResponseEntity<List<LessonPage>> getAllLessonPages(Integer id){
        return ResponseEntity.ok(lessonPageRepository.findAllByLessonId(id));
    }
}
