package com.nt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

	private String fname;
	private String lname;
	@Email(message = "Provide valid email")
	@NotBlank(message = "Email is Required")
	private String email;
	@Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is required")
	private String password;
	private Long phone;
	private String address;
	
}
