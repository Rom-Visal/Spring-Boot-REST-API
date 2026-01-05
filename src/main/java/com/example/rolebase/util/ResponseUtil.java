package com.example.rolebase.util;

import java.time.LocalDateTime;
import org.springframework.web.context.request.WebRequest;
import com.example.rolebase.dto.response.ErrorResponse;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ResponseUtil {

    public static ErrorResponse createErrorResponse(
            LocalDateTime timestamp,
            String errorType,
            String message,
            WebRequest request) {

        String path = request.getDescription(false);

        if (path.startsWith("uri=")) {
            path = path.substring(4);
        }

        return new ErrorResponse(timestamp, errorType, message, path);
    }
}