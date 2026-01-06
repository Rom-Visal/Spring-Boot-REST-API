package com.example.rolebase.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AdminRegistrationRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Email(message = "Invalid input email.")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    private Set<String> roles;
}