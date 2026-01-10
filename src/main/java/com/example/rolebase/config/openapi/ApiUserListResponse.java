package com.example.rolebase.config.openapi;

import com.example.rolebase.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "200", description = "Success",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))))
public @interface ApiUserListResponse {
}
