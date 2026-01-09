package com.example.rolebase.api;

import com.example.rolebase.dto.request.UpdateUserRequest;
import com.example.rolebase.dto.response.UpdateUserResponse;
import com.example.rolebase.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User Profile", description = "APIs for user profile management")
@SecurityRequirement(name = "basicAuth")
public interface UserApi {

    @Operation(summary = "Get Profile", description = "Get current user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    })
    ResponseEntity<UserResponse> getProfile(@Parameter(hidden = true)
                                            Authentication authentication);

    @Operation(summary = "Update Profile",
            description = "Update email or password. Account must be enabled")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated",
                    content = @Content(schema = @Schema(implementation = UpdateUserResponse.class))),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Account disabled")
    })
    ResponseEntity<UpdateUserResponse> updateProfile(
            @Valid @RequestBody UpdateUserRequest request,
            @Parameter(hidden = true) Authentication auth);
}