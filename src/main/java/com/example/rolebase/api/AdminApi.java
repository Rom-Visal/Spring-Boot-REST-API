package com.example.rolebase.api;

import com.example.rolebase.config.openapi.ApiUserListResponse;
import com.example.rolebase.config.openapi.ApiUserResponse;
import com.example.rolebase.config.openapi.SecuredEndpoint;
import com.example.rolebase.config.openapi.SecuredGetById;
import com.example.rolebase.dto.request.AdminRegistrationRequest;
import com.example.rolebase.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @SecuredEndpoint
    @ApiUserResponse
    ResponseEntity<UserResponse> getProfile(@Parameter(hidden = true)
                                            Authentication authentication);

    @Operation(summary = "Create User", description = "Create user with custom roles")
    @ApiUserResponse
    @SecuredEndpoint
    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest")
    ResponseEntity<UserResponse> createUser(@Valid @RequestBody AdminRegistrationRequest request);

    @Operation(summary = "Update User Status", description = "Enable or disable user account")
    @ApiResponse(responseCode = "200", description = "User deleted")
    @SecuredGetById
    ResponseEntity<String> updateUserStatus(
            @Parameter(description = "Username", example = "User") @PathVariable String username,
            @Parameter(description = "Enable/Disable", example = "false") @RequestParam boolean enabled);

    @Operation(summary = "Get All Users", description = "Get all users")
    @SecuredEndpoint
    @ApiUserListResponse
    ResponseEntity<List<UserResponse>> getAllUsers();

    @Operation(summary = "Get User by ID", description = "Get user by ID")
    @ApiUserResponse
    @SecuredGetById
    ResponseEntity<UserResponse> getUser(@Parameter(description = "User ID",
            example = "1") @PathVariable Integer id);

    @Operation(summary = "Delete User", description = "Permanently delete user")
    @ApiResponse(responseCode = "200", description = "User deleted")
    @SecuredGetById
    ResponseEntity<String> deleteUser(@Parameter(description = "User ID", example = "10")
                                      @PathVariable Integer id);
}