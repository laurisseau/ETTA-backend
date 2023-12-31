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

    public ResponseEntity<String> addPage(LessonPage lessonPage){

        LessonPage pageNumExist = lessonPageRepository.findByPageNum(lessonPage.getPageNum());

        if(pageNumExist != null){
            return ResponseEntity.badRequest().body("Page number has to be unique.");
        }

        int currentPageNum = lessonPage.getPageNum();
        int lessonId = lessonPage.getLessonId();
        Lesson lesson = lessonRepository.findById(lessonId);

        lesson.setNumOfPages(currentPageNum);

        lessonRepository.save(lesson);

        lessonPageRepository.save(lessonPage);

        return ResponseEntity.ok("created Successfully");

    }

    public ResponseEntity<List<LessonPage>> getAllLessonPages(Integer id){
        return ResponseEntity.ok(lessonPageRepository.findAllByLessonId(id));
    }

    public ResponseEntity<Optional<LessonPage>> getLessonPageById(Integer id){
        return ResponseEntity.ok(lessonPageRepository.findById(id));
    }

    public ResponseEntity<Optional<LessonPage>> updateLessonPageById
            (Integer id, LessonPage updatedLessonPage){

        LessonPage existingLessonPage = lessonPageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson Page not found with id: " + id));

        existingLessonPage.setPageNum(updatedLessonPage.getPageNum());
        existingLessonPage.setHeader(updatedLessonPage.getHeader());
        existingLessonPage.setTask(updatedLessonPage.getTask());
        existingLessonPage.setEditorValue(updatedLessonPage.getEditorValue());

        LessonPage savedLessonPage = lessonPageRepository.save(existingLessonPage);
        return ResponseEntity.ok(Optional.of(savedLessonPage));
    }
}
