package com.example.rolebase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rolebase.dto.response.UpdateUserResponse;
import com.example.rolebase.dto.request.UpdateUserRequest;
import com.example.rolebase.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(Authentication authentication) {
        return ResponseEntity.ok("User profile for: " + authentication.getName());
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UpdateUserResponse> updateProfile(
            @Valid @RequestBody UpdateUserRequest request, Authentication auth) {

        UpdateUserResponse updateResponse = userService.updateUser(auth.getName(), request);

        return ResponseEntity.ok(updateResponse);
    }
}