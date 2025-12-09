package com.nt.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nt.payload.ApiResponse;
import com.nt.payload.ErrorDetails;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobelExceptionHandler {

	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<Void>> handelRuntimeException(RuntimeException e) {
		log.error("Exception Accured At runtime: "+e.getMessage());
		ErrorDetails error=ErrorDetails.builder()
				.errorCode("RUNTIME_ERROR")
				.errorMessage(e.getMessage())
				.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ApiResponse.error("Operation Failed..",error));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handelValidationException(MethodArgumentNotValidException e) {
		log.error("Exception Accured At MethodArgumentNotValidException: "+e.getMessage());
		String errors = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        
        ErrorDetails error = ErrorDetails.builder()
                .errorCode("VALIDATION_ERROR")
                .errorMessage(errors)
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", error));

	}
	
	@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected exception occurred: ", ex);
        ErrorDetails error = ErrorDetails.builder()
                .errorCode("INTERNAL_ERROR")
                .errorMessage("An unexpected error occurred")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", error));
    }

}
