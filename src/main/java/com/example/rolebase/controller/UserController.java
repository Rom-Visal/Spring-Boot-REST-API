package com.example.rolebase.controller;

import com.example.rolebase.dto.request.UpdateUserRequest;
import com.example.rolebase.dto.response.UpdateUserResponse;
import com.example.rolebase.dto.response.UserResponse;
import com.example.rolebase.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('USER', 'MANAGER', 'ADMIN')")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        UserResponse getProfile = userService.getProfile(authentication.getName());
        return ResponseEntity.ok(getProfile);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UpdateUserResponse> updateProfile(
            @Valid @RequestBody UpdateUserRequest request, Authentication auth) {

        UpdateUserResponse updateResponse = userService.updateUser(auth.getName(), request);

        return ResponseEntity.ok(updateResponse);
    }
}