package com.example.rolebase.controller;

import com.example.rolebase.dto.request.AdminRegistrationRequest;
import com.example.rolebase.dto.response.UserResponse;
import com.example.rolebase.entity.User;
import com.example.rolebase.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class AdminController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        UserResponse userProfile = userService.getProfile(authentication.getName());
        return ResponseEntity.ok(userProfile);
    }

    @PostMapping("/create-user")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody AdminRegistrationRequest request) {

        User newUser = userService.registerUserByAdmin(request);
        UserResponse response = userService.toUserResponse(newUser);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/user/{username}/status")
    public ResponseEntity<String> updateUserStatus(@PathVariable String username
            , @RequestParam boolean enabled) {

        userService.updateUserStatus(username, enabled);

        String status = enabled ? "enabled" : "disabled";
        return ResponseEntity.ok("User '" +
                username + "' has been " + status + " successfully.");
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> getResponseList = userService.getAll();
        return ResponseEntity.ok(getResponseList);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
    }
}