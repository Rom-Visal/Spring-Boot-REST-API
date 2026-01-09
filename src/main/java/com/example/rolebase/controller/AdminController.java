package com.example.rolebase.controller;

import com.example.rolebase.api.AdminApi;
import com.example.rolebase.dto.request.AdminRegistrationRequest;
import com.example.rolebase.dto.response.UserResponse;
import com.example.rolebase.entity.User;
import com.example.rolebase.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final UserService userService;

    @Override
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        UserResponse userProfile = userService.getProfile(authentication.getName());
        return ResponseEntity.ok(userProfile);
    }

    @Override
    @PostMapping("/create-user")
    public ResponseEntity<UserResponse> createUser(AdminRegistrationRequest request) {
        User newUser = userService.registerUserByAdmin(request);
        UserResponse response = userService.toUserResponse(newUser);
        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/user/{username}/status")
    public ResponseEntity<String> updateUserStatus(@PathVariable String username, boolean enabled) {
        userService.updateUserStatus(username, enabled);
        String status = enabled ? "enabled" : "disabled";
        return ResponseEntity.ok("User '" + username + "' has been " + status + " successfully.");
    }

    @Override
    @GetMapping("/user/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> getResponseList = userService.getAll();
        return ResponseEntity.ok(getResponseList);
    }

    @Override
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Override
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
    }
}