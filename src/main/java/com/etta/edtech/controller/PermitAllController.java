package com.etta.edtech.controller;

import com.etta.edtech.model.Compiler;
import com.etta.edtech.model.Lesson;
import com.etta.edtech.repository.LessonRepository;
import com.etta.edtech.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/permitAll")
public class PermitAllController {

    private final AdminService adminService;
    private final LessonRepository lessonRepository;
    @PostMapping("/compiler")
    public String compiler(@RequestBody Compiler compiler) throws InterruptedException, ExecutionException {
        //compiler.setLanguage("nodejs");
        compiler.setClientId("16ed8bf5307927a66d64cbc90685cb91");
        compiler.setClientSecret("ad9fde873027f087038f1167b64b10b1aaa861b463b17e46bf86a29234436b0");
        compiler.setVersionIndex("0");
        String code = compiler.getCode();
        String language = compiler.getLanguage();
        compiler.setLanguage(language);
        String apiUrl = "https://api.jdoodle.com/v1/execute";

        String input = String.format(
                "{\"clientId\": \"%s\",\"clientSecret\":\"%s\",\"script\":\"%s\",\"language\":\"%s\",\"versionIndex\":\"%s\"}",
                compiler.getClientId(), compiler.getClientSecret(), code, compiler.getLanguage(), compiler.getVersionIndex());

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(input, headers);

            RestTemplate restTemplate = new RestTemplate();

            CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->
                    restTemplate.postForObject(apiUrl, request, String.class));

            return future.get();

        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/lessons")
    public ResponseEntity<List<Lesson>> getAllLessons() {
        return ResponseEntity.ok(lessonRepository.findAll());
    }





}
