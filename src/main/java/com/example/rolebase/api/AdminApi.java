package com.example.rolebase.api;

import com.example.rolebase.dto.request.AdminRegistrationRequest;
import com.example.rolebase.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Admin Management", description = "Admin-only APIs for user management")
@SecurityRequirement(name = "basicAuth")
public interface AdminApi {

    @Operation(summary = "Get Profile", description = "Get admin profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    })
    ResponseEntity<UserResponse> getProfile(@Parameter(hidden = true)
                                            Authentication authentication);

    @Operation(summary = "Create User", description = "Create user with custom roles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    })
    ResponseEntity<UserResponse> createUser(@Valid @RequestBody AdminRegistrationRequest request);

    @Operation(summary = "Update User Status", description = "Enable or disable user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    })
    ResponseEntity<String> updateUserStatus(
            @Parameter(description = "Username", example = "john_doe") @PathVariable String username,
            @Parameter(description = "Enable/Disable", example = "false") @RequestParam boolean enabled);

    @Operation(summary = "Get All Users", description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    })
    ResponseEntity<List<UserResponse>> getAllUsers();

    @Operation(summary = "Get User by ID", description = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    })
    ResponseEntity<UserResponse> getUser(@Parameter(description = "User ID", example = "1") @PathVariable Integer id);

    @Operation(summary = "Delete User", description = "Permanently delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    })
    ResponseEntity<String> deleteUser(@Parameter(description = "User ID", example = "10") @PathVariable Integer id);
}