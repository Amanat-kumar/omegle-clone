package com.omegleclone.service;

import com.omegleclone.dto.LoginResponse;
import com.omegleclone.model.AppUser;
import com.omegleclone.repository.AppUserRepository;
import com.omegleclone.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class AppUserService {

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	// Register a new user
	@Transactional
	public AppUser registerUser(AppUser user) {
		// Encrypt the password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return appUserRepository.save(user);
	}

	// Login user (here you'd return a JWT token in a real application)
	public LoginResponse loginUser(AppUser user) {
		AppUser foundUser = appUserRepository.findByUsername(user.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
			// Return a JWT in a real application
			return jwtUtil.generateToken(foundUser);
		} else {
			throw new RuntimeException("Invalid credentials");
		}
	}

	// Get user details by ID
	public AppUser getUserById(Long id) {
		return appUserRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
	}

	// Get all users
	public List<AppUser> getAllUsers() {
		return appUserRepository.findAll();
	}
}
