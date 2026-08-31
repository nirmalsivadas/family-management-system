package com.family_management_system.family_service.exception;

import com.family_management_system.family_service.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request
    ){
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMessage(ex.getMessage());
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ApiErrorResponse> handleMailException(
            MailException ex, HttpServletRequest request
    ){
        ApiErrorResponse response = new ApiErrorResponse();
        String message = request.getRequestURI().contains("forgot-password")
                ? "Temporary password email could not be sent. Please check mail settings."
                : "Password changed, but confirmation email could not be sent. Please check mail settings.";
        response.setMessage(message);
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.BAD_GATEWAY);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request
    ){
        Map<String,String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            fieldErrors.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMessage("Validation failed for one or more fields");
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setErrors(fieldErrors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(
            Exception ex, HttpServletRequest request
    ){
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMessage(ex.getMessage() != null ? ex.getMessage() : "Request failed");
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
