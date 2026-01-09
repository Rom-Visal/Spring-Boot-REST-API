package com.example.rolebase.config;

import com.example.rolebase.dto.response.ErrorResponse;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "basicAuth";

        Schema<?> errorSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new io.swagger.v3.core.converter.AnnotatedType(ErrorResponse.class))
                .schema;

        return new OpenAPI()
                .info(new Info()
                        .title("RBAC Management System API")
                        .version("1.0")
                        .description("Role-Based Access Control API with User, Manager, and Admin roles"))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic"))
                        .addSchemas("ErrorResponse", errorSchema)
                        .addResponses("BadRequest", new ApiResponse()
                                .description("Invalid request or validation error")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                        .addResponses("Unauthorized", new ApiResponse()
                                .description("Authentication required")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                        .addResponses("Forbidden", new ApiResponse()
                                .description("Access denied")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                        .addResponses("NotFound", new ApiResponse()
                                .description("Resource not found")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse")))))
                        .addResponses("InternalServerError", new ApiResponse()
                                .description("Internal server error")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))))));
    }
}