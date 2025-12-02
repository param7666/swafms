package com.nt.service;

import java.util.List;

import com.nt.dto.UserRequestDTO;
import com.nt.dto.UserResponseDTO;

public interface IUserService {

	UserResponseDTO createUser(UserRequestDTO request);

    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRequestDTO request);

    String deleteUser(Long id);

    UserResponseDTO getUserByEmail(String email);
}
