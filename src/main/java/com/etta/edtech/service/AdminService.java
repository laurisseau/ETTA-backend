package com.etta.edtech.service;

import com.amazonaws.services.accessanalyzer.model.ResourceNotFoundException;
import com.etta.edtech.exceptions.DuplicatePageNumberException;
import com.etta.edtech.exceptions.EmptyFieldException;
import com.etta.edtech.model.Lesson;
import com.etta.edtech.model.LessonPage;
import com.etta.edtech.repository.LessonPageRepository;
import com.etta.edtech.repository.LessonRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
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
        if(lesson.getName().isEmpty() || lesson.getSubscription().isEmpty()
                || lesson.getLanguage().isEmpty() || lesson.getDescription().isEmpty()){
            throw new EmptyFieldException("Please fill out all required fields");
        }
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

    @Transactional
    public String deleteLessonById(Integer id){
        lessonPageRepository.deleteAllByLessonIdId(id);

        lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));

        lessonRepository.deleteById(id);

        return "deleted";
    }

    public String deletePageById(Integer id){
        lessonPageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));

        LessonPage lessonPage = lessonPageRepository.findLessonPageById(id);

        Lesson lesson = lessonPage.getLessonId();

        int lessonId = lesson.getId();

        List<LessonPage>  allLessonPages = lessonPageRepository.findAllByLessonIdId(lessonId);

        lesson.setNumOfPages(allLessonPages.size() - 1);

        lessonPageRepository.deleteById(id);

        return "deleted";
    }

    public ResponseEntity<String> addPage(LessonPage lessonPage){

        Lesson lesson = lessonPage.getLessonId();

        int lessonId = lesson.getId();

        List<LessonPage>  allLessonPages = lessonPageRepository.findAllByLessonIdId(lessonId);

        for(int i = 0; i < allLessonPages.size(); i++){
            if(lessonPage.getPageNum() == allLessonPages.get(i).getPageNum()){
                throw new DuplicatePageNumberException("Page number has to be unique.");
            }
        }

        Lesson findLesson = lessonRepository.findById(lessonId);

        findLesson.setNumOfPages(allLessonPages.size() + 1);

        lessonRepository.save(findLesson);

        lessonPageRepository.save(lessonPage);

        return ResponseEntity.ok("created Successfully");

    }

    public ResponseEntity<List<LessonPage>> getAllLessonPages(Integer id){
        Sort sort = Sort.by(Sort.Order.asc("pageNum"));
        return ResponseEntity.ok(lessonPageRepository.findAllByLessonIdId(id, sort));
    }

    public ResponseEntity<Optional<LessonPage>> getLessonPageById(Integer id){
        return ResponseEntity.ok(lessonPageRepository.findById(id));
    }

    public ResponseEntity<String> updateLessonPageById
            (Integer id, LessonPage updatedLessonPage){

        LessonPage existingLessonPage = lessonPageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson Page not found with id: " + id));

        existingLessonPage.setPageNum(updatedLessonPage.getPageNum());
        existingLessonPage.setHeader(updatedLessonPage.getHeader());
        existingLessonPage.setLessonInfo(updatedLessonPage.getLessonInfo());
        existingLessonPage.setTask(updatedLessonPage.getTask());
        existingLessonPage.setEditorValue(updatedLessonPage.getEditorValue());
        existingLessonPage.setSubHeader(updatedLessonPage.getSubHeader());

        // Check page number uniqueness
        if (!isPageNumberUnique(updatedLessonPage.getLessonId().getId(), updatedLessonPage.getPageNum())) {
            return ResponseEntity.badRequest().body("Page number has to be unique.");
        }

        lessonPageRepository.save(existingLessonPage);

        return ResponseEntity.ok("Lesson page updated");
    }

    private boolean isPageNumberUnique(int lessonId, int updatedPageNum) {
        List<LessonPage> allLessonPages = lessonPageRepository.findAllByLessonIdId(lessonId);

        long count = allLessonPages.stream()
                .filter(page -> page.getPageNum() == updatedPageNum)
                .count();

        return count <= 1;
    }
}
