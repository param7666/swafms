package com.nt.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.nt.payload.ApiResponse;
import com.nt.payload.ApiResponse.ErrorDetails;
import com.nt.payload.ApiResponse.ValidationError;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobleExceptioinHandler {

	
	@ExceptionHandler(InventoryNotFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handelInventoryNotFound(InventoryNotFoundException e,WebRequest req) {
		log.error("Inventory not found: {}", e.getMessage());
		
		ErrorDetails error=ErrorDetails.builder()
				.code("INVENTORY_NOT_FOUND")
				.details(e.getMessage())
				.build();
		
		ApiResponse<Object> response=ApiResponse.error(
				"INVENTORY_NOT_FOUND",error,req.getDescription(false).replace("uri=", ""));
		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	
	public ResponseEntity<ApiResponse<Object>> handelInsufficientStockException(InsufficientStockException e,WebRequest req) {
		log.error("Insufficient stock: {}", e.getMessage());
		
		ErrorDetails error=ErrorDetails.builder()
				.code("INSUFFICIENT_STOCK_EXCEPTION")
				.details(String.format("SKU: %s, Requested: %d, Available: %d", 
                        e.getSkuCode(), e.getRequested(), e.getAvailable()))
				.build();
		
		ApiResponse<Object> response = ApiResponse.error(
                e.getMessage(),
                error,
                req.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    
	}
	
	 @ExceptionHandler(DuplicateInventoryException.class)
	    public ResponseEntity<ApiResponse<Object>> handleDuplicateInventory(
	            DuplicateInventoryException ex, WebRequest request) {
	        log.error("Duplicate inventory: {}", ex.getMessage());
	        
	        ErrorDetails error = ErrorDetails.builder()
	                .code("DUPLICATE_INVENTORY")
	                .details(ex.getMessage())
	                .build();
	        
	        ApiResponse<Object> response = ApiResponse.error(
	                "Duplicate inventory entry",
	                error,
	                request.getDescription(false).replace("uri=", "")
	        );
	        
	        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	    }

	 
	 @ExceptionHandler(InvalidInventoryOperationException.class)
	    public ResponseEntity<ApiResponse<Object>> handleInvalidOperation(
	            InvalidInventoryOperationException ex, WebRequest request) {
	        log.error("Invalid inventory operation: {}", ex.getMessage());
	        
	        ErrorDetails error = ErrorDetails.builder()
	                .code("INVALID_OPERATION")
	                .details(ex.getMessage())
	                .build();
	        
	        ApiResponse<Object> response = ApiResponse.error(
	                "Invalid inventory operation",
	                error,
	                request.getDescription(false).replace("uri=", "")
	        );
	        
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    
	 @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
	            MethodArgumentNotValidException ex, WebRequest request) {
	        log.error("Validation error: {}", ex.getMessage());
	        
	        List<ValidationError> validationErrors = ex.getBindingResult()
	                .getFieldErrors()
	                .stream()
	                .map(error -> ValidationError.builder()
	                        .field(error.getField())
	                        .message(error.getDefaultMessage())
	                        .rejectedValue(error.getRejectedValue())
	                        .build())
	                .collect(Collectors.toList());
	        
	        ErrorDetails error = ErrorDetails.builder()
	                .code("VALIDATION_ERROR")
	                .details("Invalid input parameters")
	                .validationErrors(validationErrors)
	                .build();
	        
	        ApiResponse<Object> response = ApiResponse.error(
	                "Validation failed",
	                error,
	                request.getDescription(false).replace("uri=", "")
	        );
	        
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(ConstraintViolationException.class)
	    public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(
	            ConstraintViolationException ex, WebRequest request) {
	        log.error("Constraint violation: {}", ex.getMessage());
	        
	        List<ValidationError> validationErrors = ex.getConstraintViolations()
	                .stream()
	                .map(violation -> ValidationError.builder()
	                        .field(violation.getPropertyPath().toString())
	                        .message(violation.getMessage())
	                        .rejectedValue(violation.getInvalidValue())
	                        .build())
	                .collect(Collectors.toList());
	        
	        ErrorDetails error = ErrorDetails.builder()
	                .code("CONSTRAINT_VIOLATION")
	                .details("Constraint violation occurred")
	                .validationErrors(validationErrors)
	                .build();
	        
	        ApiResponse<Object> response = ApiResponse.error(
	                "Validation failed",
	                error,
	                request.getDescription(false).replace("uri=", "")
	        );
	        
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
	    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(
	            MethodArgumentTypeMismatchException ex, WebRequest request) {
	        log.error("Type mismatch: {}", ex.getMessage());
	        
	        String error = String.format("Parameter '%s' should be of type '%s'",
	                ex.getName(),
	                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
	        
	        ErrorDetails errorDetails = ErrorDetails.builder()
	                .code("TYPE_MISMATCH")
	                .details(error)
	                .build();
	        
	        ApiResponse<Object> response = ApiResponse.error(
	                "Invalid parameter type",
	                errorDetails,
	                request.getDescription(false).replace("uri=", "")
	        );
	        
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
	    
	    @ExceptionHandler(InventoryServiceException.class)
	    public ResponseEntity<ApiResponse<Object>> handleInventoryServiceException(
	            InventoryServiceException ex, WebRequest request) {
	        log.error("Inventory service error: {}", ex.getMessage(), ex);
	        
	        ErrorDetails error = ErrorDetails.builder()
	                .code("SERVICE_ERROR")
	                .details(ex.getMessage())
	                .build();
	        
	        ApiResponse<Object> response = ApiResponse.error(
	                "Service error occurred",
	                error,
	                request.getDescription(false).replace("uri=", "")
	        );
	        
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    
	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiResponse<Object>> handleGlobalException(
	            Exception ex, WebRequest request) {
	        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
	        
	        ErrorDetails error = ErrorDetails.builder()
	                .code("INTERNAL_ERROR")
	                .details("An unexpected error occurred. Please try again later.")
	                .build();
	        
	        ApiResponse<Object> response = ApiResponse.error(
	                "Internal server error",
	                error,
	                request.getDescription(false).replace("uri=", "")
	        );
	        
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }


}
