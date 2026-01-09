package com.example.rolebase.api;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Manager Operations", description = "APIs for viewing users")
@SecurityRequirement(name = "basicAuth")
public interface ManagerApi {

    @Operation(summary = "Get All Users", description = "Retrieve all users in system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden")
    })
    ResponseEntity<List<UserResponse>> getAllUsers();

    @Operation(summary = "Get User by ID", description = "Get user details by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/NotFound")
    })
    ResponseEntity<UserResponse> getUser(@Parameter(description = "User ID", example = "1") @PathVariable Integer id);

    @Operation(summary = "Get Profile", description = "Get manager's own profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized")
    })
    ResponseEntity<UserResponse> getProfile(@Parameter(hidden = true) Authentication authentication);
}