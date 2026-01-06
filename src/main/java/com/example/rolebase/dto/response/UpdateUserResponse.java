package com.example.rolebase.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserResponse {

    private UserResponse beforeUpdate;
    private UserResponse afterUpdate;

    @Builder.Default
    private String message = "Profile updated successfully";

}