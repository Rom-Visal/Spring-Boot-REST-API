package com.example.rolebase.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AdminRegistrationRequest {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 50, message = "Username must be between 2-50 characters")
    private String username;

    @Email(message = "Invalid input email.")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    private Set<String> roles;
}