package com.family_management_system.auth_service.exception;

import com.family_management_system.auth_service.dto.ApiErrorResponse;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request
    ) {
        return buildResponse("Invalid email or password", HttpStatus.UNAUTHORIZED, request, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildResponse("Validation failed for one or more fields",
                HttpStatus.BAD_REQUEST,
                request,
                fieldErrors);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorResponse> handleFeignException(
            FeignException ex, HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        String message = "User service is unavailable. Please try again shortly.";

        if (ex.status() >= 400 && ex.status() < 500) {
            status = HttpStatus.BAD_GATEWAY;
            message = "User service rejected the login request.";
        }

        return buildResponse(message, status, request, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(
            Exception ex, HttpServletRequest request
    ) {
        return buildResponse(
                ex.getMessage() != null ? ex.getMessage() : "Request failed",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request,
                null);
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            String message,
            HttpStatus status,
            HttpServletRequest request,
            Map<String, String> errors
    ) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMessage(message);
        response.setStatus(status);
        response.setPath(request.getRequestURI());
        response.setTimeStamp(LocalDateTime.now());
        response.setErrors(errors);
        return ResponseEntity.status(status).body(response);
    }
}
