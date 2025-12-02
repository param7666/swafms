package com.nt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nt.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Query(" from User where email=?1")
	public User findByEmail(String email);
}
