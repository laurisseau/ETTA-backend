package com.etta.edtech.controller;

import com.etta.edtech.model.Lesson;
import com.etta.edtech.model.LessonPage;
import com.etta.edtech.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    @PostMapping("/createLesson")
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        return adminService.createLesson(lesson);
    }
    //
    @GetMapping("/lesson/{id}")
    public ResponseEntity<Optional<Lesson>> getLessonById(@PathVariable Integer id) {
        return adminService.getLessonById(id);
    }
    @DeleteMapping("/deleteLesson/{id}")
    public String deleteLessonById(@PathVariable Integer id) {
        return adminService.deleteLessonById(id);
    }

    @DeleteMapping("/deletePage/{id}")
    public String deletePageById(@PathVariable Integer id) {
        return adminService.deletePageById(id);
    }

    @PutMapping("/updateLesson/{id}")
    public ResponseEntity<Lesson> updateLessonById(@PathVariable Integer id, @RequestBody Lesson updatedLesson) {
        return adminService.updateLessonById(id, updatedLesson);
    }
    @PostMapping("/addPage")
    public ResponseEntity<String> addLessonPage(@RequestBody LessonPage lessonPage){
        return adminService.addPage(lessonPage);
    }

    @GetMapping("/lessonPages/{id}")
    public ResponseEntity<List<LessonPage>> getAllLessonPages(@PathVariable Integer id) {
        return adminService.getAllLessonPages(id);
    }

    @GetMapping("/lessonPage/{id}")
    public ResponseEntity<Optional<LessonPage>> getLessonPageById(@PathVariable Integer id){
        return adminService.getLessonPageById(id);
    }

    @PutMapping("/lessonPage/{id}")
    public ResponseEntity<String> updateLessonPageById
            (@PathVariable Integer id, @RequestBody LessonPage updatedLessonPage){
        return adminService.updateLessonPageById(id, updatedLessonPage);
    }
}
