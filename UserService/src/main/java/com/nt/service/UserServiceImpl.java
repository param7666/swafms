package com.nt.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.nt.UserServiceApplication;
import com.nt.dto.UserRequestDTO;
import com.nt.dto.UserResponseDTO;
import com.nt.entity.User;
import com.nt.exception.InvalidEmailIdException;
import com.nt.exception.RequestNullException;
import com.nt.exception.UserNotFoundException;
import com.nt.repository.UserRepository;
import org.slf4j.*;

@Service
public class UserServiceImpl implements IUserService {

	private final UserRepository userRepo;
	private final Logger logger=LoggerFactory.getLogger("UserServiceImpl");
	
	public UserServiceImpl(UserRepository repo, UserServiceApplication userServiceApplication) {
		logger.info("Contrctor Execeuted UserServiceIMPL");
		this.userRepo=repo;
	}
	
	private UserResponseDTO convertToResponse(User user) {
		logger.info("UserServiceImpl.convertToResponse()");
		UserResponseDTO dto=new UserResponseDTO();
		BeanUtils.copyProperties(user, dto);
		return dto;
	}
	
	@Override
	public UserResponseDTO createUser(UserRequestDTO request) {
		logger.info("UserServiceImpl.createUser()");
		if(request==null) throw new RequestNullException("Request Is Null");
		try {
			User user=new User();
			BeanUtils.copyProperties(request, user);
			User saved=userRepo.save(user);
			return convertToResponse(saved);
		} catch(Exception e) {
			 logger.error("Error creating user: {}", e.getMessage());
		     throw new RuntimeException("Failed to create user", e); // <-- FIX
			
		}
	}

	@Override
	public UserResponseDTO getUserById(Long id) {
		logger.info("UserServiceImpl.getUserById()");
		User user=userRepo.findById(id).orElseThrow(()->  new RuntimeException("User Not Found"));
		return convertToResponse(user);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		logger.info("UserServiceImpl.getAllUsers()");
		try {
			return  userRepo.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
		} catch(Exception e) {
			logger.info("Error Generated :: "+e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
		logger.info("UserServiceImpl.updateUser()");
		try {
			User u=userRepo.findById(id).orElseThrow(()-> new  UserNotFoundException("User Not Found"));
			BeanUtils.copyProperties(request, u);
			User updated=userRepo.save(u);
			return convertToResponse(updated);
		} catch(Exception e) {
			logger.info(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public String deleteUser(Long id) {
		logger.info("UserServiceImpl.deleteUser()");
		try {
			userRepo.deleteById(id);
			return "User deleted belongs to "+id;
		}catch(Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public UserResponseDTO getUserByEmail(String email) {
		logger.info("UserServiceImpl.getUserByEmail()");
		try {
			 User user=userRepo.findByEmail(email);
			 if(user==null) throw new InvalidEmailIdException("Email not found.");
			 return convertToResponse(user);
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

}
