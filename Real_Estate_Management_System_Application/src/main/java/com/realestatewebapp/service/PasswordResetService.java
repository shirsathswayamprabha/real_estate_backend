package com.realestatewebapp.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.realestatewebapp.model.User;
import com.realestatewebapp.repository.UserRepository;

@Service
public class PasswordResetService {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public String generateResetToken(String email) throws Exception{
		Optional<User> userOpt = userRepository.findByEmail(email);
		
		if(!userOpt.isPresent()) {
			throw new Exception("User not found");
			
		}
		
		User user = userOpt.get();
		String token = UUID.randomUUID().toString();
		System.out.println("token"+token);
		user.setResetToken(token);
		user.setTokenExpiryDate(LocalDateTime.now().plusMinutes(30));
		userRepository.save(user);
		return token;
		
//		sendPasswordResetEmail(user.getEmail(), token);
	}
	
//	private void sendPasswordResetEmail(String email, String token) {
//		
//		String resetUrl = "http://localhost:5173/reset-password/" + token;
//		System.out.println("Sending email to " + email + " with reset URL: " + resetUrl);
//	}
	
	public void updatePassword(String token, String newPassword, String confirmPassword) throws Exception {
		Optional<User> userOpt = userRepository.findByResetToken(token);
		
		if(!userOpt.isPresent()) {
			throw new Exception("Invalid Token");
			
		}
		
		User user = userOpt.get();
		
		if(user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
			   throw new Exception("Token expired");
		}
		
		if(!newPassword.equals(confirmPassword)) {
			  throw new Exception("Passwords do not match");
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setResetToken(null);
		user.setTokenExpiryDate(null);
		userRepository.save(user);
	}

}
