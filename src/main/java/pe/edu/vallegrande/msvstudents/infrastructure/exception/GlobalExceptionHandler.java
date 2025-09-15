package pe.edu.vallegrande.msvstudents.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import pe.edu.vallegrande.msvstudents.infrastructure.dto.response.ApiResponse;

import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(WebExchangeBindException ex) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            validationErrors.put(error.getField(), error.getDefaultMessage())
        );
        
        ApiResponse<Map<String, String>> response = ApiResponse.success(validationErrors, "Validation failed", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiResponse<Void> response = ApiResponse.error("Constraint validation failed: " + ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<Void> response = ApiResponse.error("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.error("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 