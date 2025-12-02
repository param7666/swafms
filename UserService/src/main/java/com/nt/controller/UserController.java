package com.nt.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nt.dto.UserRequestDTO;
import com.nt.dto.UserResponseDTO;
import com.nt.service.IUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final IUserService service;
	private final Logger logger=LoggerFactory.getLogger(UserController.class);
	
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody UserRequestDTO request) {
		logger.info("UserController.createUser()");
		if(request==null) {
			return new ResponseEntity<String>("Request is null", HttpStatus.BAD_REQUEST);
		}
		
//		if(request.getFName()==null || request.getLName()==null||request.getEmail()==null||request.getPassword()==null) {
//			logger.error("Invalid Deetails:: ");
//			return new ResponseEntity<String>("Invalid Details",HttpStatus.BAD_REQUEST);
//		}
		
		try {
			UserResponseDTO response=service.createUser(request);
			return  ResponseEntity.status(HttpStatus.OK).body(response);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return new ResponseEntity<String>("Internal Server Error::"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	// GET USER BY ID
	@GetMapping("/{id}")
	public ResponseEntity<?> findUserById(@PathVariable Long id) {
		logger.info("UserController.findUserById()");
		if(id==null || id<0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Id");
		}
		
		try {
		UserResponseDTO resp=service.getUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body(resp);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	
	// get all the users
	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers() {
		logger.info("UserController.getAllUsers()");
		try {
			List<UserResponseDTO> list=service.getAllUsers();
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	
	//Delete the users
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUsers(@PathVariable Long id) {
		logger.info("UserController.deleteUsers()");
		if(id==null || id<0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
		try {
			String result=service.deleteUser(id);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	
	// update User
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO req) {
		
		System.out.println("UserController.updateUser()");
		
		logger.info("UserController.updateUser()");
		try {
			UserResponseDTO res=service.updateUser(id, req);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	// get by email
	@GetMapping("/email/{email}")
	public ResponseEntity<?> getUserByEmailId(@PathVariable String email) {
		logger.info("UserController.getUserByEmailId()");
		if(email==null || email.isBlank() || email.isEmpty()) {
			return new ResponseEntity<String>("Email Can not be null", HttpStatus.BAD_REQUEST);
		}
		try {
			UserResponseDTO res=service.getUserByEmail(email);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch(Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
}
