package com.nt.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nt.dto.UserDTO;
import com.nt.payload.ApiResponse;

@FeignClient(
		name = "UserService",
		path = "/api/user")
public interface UserFeingClient {

	@GetMapping("/{id}")
	public ApiResponse<UserDTO> getUserById(Long id)throws Exception;
	
	@GetMapping("/email/{email}")
	public UserDTO getUserByEmailId(String email)throws Exception;
	
	@DeleteMapping("/delete/{id}")
	public String deleteUserById(Long id)throws Exception;
	
	@PutMapping("/update/{id}")
	public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO req)throws Exception;
	
	@GetMapping("/all")
	public List<UserDTO> getAllUsers() throws Exception;
}
