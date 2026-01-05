package com.example.rolebase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UpdateUserResponse {

    private UserResponse beforeUpdate;
    private UserResponse afterUpdate;

    @Builder.Default
    private String message = "Profile updated successfully";

}