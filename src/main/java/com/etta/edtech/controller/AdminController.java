package com.etta.edtech.controller;

import com.amazonaws.services.accessanalyzer.model.ResourceNotFoundException;
import com.etta.edtech.model.Lesson;
import com.etta.edtech.repository.LessonRepository;
import com.etta.edtech.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final LessonRepository lessonRepository;
    @PostMapping("/createLesson")
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        return adminService.createLesson(lesson);
    }
    @DeleteMapping("/deleteLesson/{id}")
    public String deleteLesson(@PathVariable Integer id) {
        lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));

        lessonRepository.deleteById(id);

        return "deleted";
    }

    @PutMapping("/updateLesson/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Integer id, @RequestBody Lesson updatedLesson) {
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

}
