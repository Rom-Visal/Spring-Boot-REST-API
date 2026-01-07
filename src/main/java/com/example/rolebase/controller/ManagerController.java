package com.example.rolebase.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class ManagerController {

    @GetMapping("/reports")
    public ResponseEntity<String> viewReport() {
        return ResponseEntity.ok("View Reports");
    }

    @PostMapping("/reports")
    public ResponseEntity<String> createReport() {
        return ResponseEntity.ok("New report created");
    }
}