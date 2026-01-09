package com.example.rolebase.controller;

import com.example.rolebase.dto.response.UserResponse;
import com.example.rolebase.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manager")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
public class ManagerController {

    private final UserService userService;

    @GetMapping("/user/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> getResponseList = userService.getAll();
        return ResponseEntity.ok(getResponseList);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        UserResponse userProfile = userService.getProfile(authentication.getName());
        return ResponseEntity.ok(userProfile);
    }
}