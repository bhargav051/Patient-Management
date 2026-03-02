package com.pm.auth_service.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pm.auth_service.dto.LoginRequestDTO;
import com.pm.auth_service.model.User;
import com.pm.auth_service.util.JwtUtil;

import io.jsonwebtoken.JwtException;

@Service
public class AuthService {
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	// Depndency injection using contructors
	public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {

	    Optional<User> userOpt =
	            userService.findByEmail(loginRequestDTO.getEmail().trim());

	    if (userOpt.isEmpty()) {
	        return Optional.empty();
	    }

	    User user = userOpt.get();

	    if (!passwordEncoder.matches(
	            loginRequestDTO.getPassword(),
	            user.getPassword())) {
	        return Optional.empty();
	    }

	    return Optional.of(
	            jwtUtil.generateToken(user.getEmail(), user.getRole())
	    );
	}
	
	public boolean validateToken(String token) {
		try {
			jwtUtil.validateToken(token);
			return true;
		} catch(JwtException e) {
			return false;
		}
	}
}
