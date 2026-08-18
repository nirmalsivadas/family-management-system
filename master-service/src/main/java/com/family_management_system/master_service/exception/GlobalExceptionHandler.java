package com.family_management_system.master_service.exception;

import com.family_management_system.master_service.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException
            (DataIntegrityViolationException ex, HttpServletRequest request){
        Map<String,String> fieldErrors = new HashMap<>();
        String rootMessage = ex.getMostSpecificCause().getMessage();
        if (rootMessage.contains("duplicate key") || rootMessage.contains("UK_") ||
                rootMessage.contains("Violation of UNIQUE KEY")){
            fieldErrors.put("name","This value already exists in database");
        }else {
            fieldErrors.put("database","Database constraint violation occurred");
        }
        ApiErrorResponse response = new ApiErrorResponse();
        response.setMessage("Database operation failed due to invalid data");
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setPath(request.getRequestURI());
        response.setErrors(fieldErrors);
        return ResponseEntity.badRequest().body(response);
    }
}
