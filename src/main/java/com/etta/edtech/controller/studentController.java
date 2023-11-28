package com.etta.edtech.controller;

import com.etta.edtech.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class studentController {

    @PostMapping("/get")
    public ResponseEntity<String> signUp() {
        return ResponseEntity.ok("Im a student");
    }
}
