package com.etta.edtech.controller;

import com.etta.edtech.config.CompilerConfig;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/permitAll")
public class PermitAllController {

    private final AdminService adminService;
    private final LessonRepository lessonRepository;
    private final CompilerConfig compilerConfig;
    @PostMapping("/compiler")
    public String compiler(@RequestBody Compiler compiler) throws InterruptedException, ExecutionException {
        String apiUrl = "https://api.jdoodle.com/v1/execute";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestPayload = new HashMap<>();

        requestPayload.put("clientId", compilerConfig.getClientId());
        requestPayload.put("clientSecret", compilerConfig.getClientSecret());

        requestPayload.put("script", compiler.getCode());
        requestPayload.put("language", compiler.getLanguage());
        requestPayload.put("versionIndex", "3");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestPayload, headers);

        RestTemplate restTemplate = new RestTemplate();


        CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->
                restTemplate.postForObject(apiUrl, request, String.class));

        return future.get();

    }

    @GetMapping("/lessons")
    public ResponseEntity<List<Lesson>> getAllLessons() {
        return ResponseEntity.ok(lessonRepository.findAll());
    }





}
