package com.nt.payload;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
	
	private String message;
	
	private String status;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime timestamp;
	
	private T data;
	
	private ErrorDetails error;

	public static <T> ApiResponse<T> success(String msg, T data){
		return ApiResponse.<T>builder()
				.message(msg)
				.status("SUCCESS")
				.timestamp(LocalDateTime.now())
				.data(data)
				.build();
	}
	
	public static <T> ApiResponse<T> error(String msg, ErrorDetails error) {
		return ApiResponse.<T>builder()
				.message(msg)
				.status("ERROR")
				.timestamp(LocalDateTime.now())
				.error(error)
				.build();
	}
	
	public static <T> ApiResponse<T> failure(String message) {
        return ApiResponse.<T>builder()
                .message(message)
                .status("FAILURE")
                .timestamp(LocalDateTime.now())
                .build();
    }

}
