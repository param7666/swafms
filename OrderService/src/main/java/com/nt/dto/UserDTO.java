package com.nt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDTO {
	
	private Long id;
	
	private String fname;
	
	private String lname;
	
	private String email;
	
	private Long phone;
	
	private String address;
}