package com.example.rolebase.controller;

import com.example.rolebase.dto.request.RegistrationRequest;
import com.example.rolebase.dto.response.UserResponse;
import com.example.rolebase.entity.User;
import com.example.rolebase.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/home")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to Spring Boot REST APIs");
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegistrationRequest request) {

        User newUser = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.toUserResponse(newUser));
    }
}