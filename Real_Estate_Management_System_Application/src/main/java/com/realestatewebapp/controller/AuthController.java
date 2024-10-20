package com.realestatewebapp.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.realestatewebapp.model.AgentDTO;
import com.realestatewebapp.model.BuyerDTO;
import com.realestatewebapp.model.LoginRequest;
import com.realestatewebapp.model.Property;
import com.realestatewebapp.model.Role;
import com.realestatewebapp.model.SellerDTO;
import com.realestatewebapp.model.User;
import com.realestatewebapp.model.UserDTO;
import com.realestatewebapp.repository.PropertyRepository;
import com.realestatewebapp.repository.UserRepository;
import com.realestatewebapp.response.ApiResponse;
import com.realestatewebapp.service.EmailService;
import com.realestatewebapp.service.PasswordResetService;
import com.realestatewebapp.service.UserService;



@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private PasswordResetService passwordResetService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PropertyRepository propertyRepository;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {

		if(userRepository.existsByEmail(user.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "User is already registered"));
		}

		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		user.setConfirmpassword(new BCryptPasswordEncoder().encode(user.getConfirmpassword()));
		userService.save(user);

		return ResponseEntity.ok(Map.of("message", "User registered successfully"));
	}


	@PostMapping("/login")
	public ApiResponse login(@RequestBody LoginRequest loginRequest) {
		Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

		if(userOptional.isPresent()) {
			User user = userOptional.get();	

			if(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
				Map<String, Object> data = new HashMap<>();
				data.put("role", user.getRole());
				data.put("userId", user.getId());

				return new ApiResponse("Login successful", data);
			}
		}

		return new ApiResponse("Invalid credentials", null);
	}


	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestParam String email) {
		try {
			String token = passwordResetService.generateResetToken(email);
			emailService.sendTokenEmail(email, token);

			return ResponseEntity.ok(Map.of("message", "Reset password email sent!"));
		}catch (Exception e) {
			return ResponseEntity.ok(Map.of("message", e.getMessage()));
		}
	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam String token,
			@RequestParam String newPassword,
			@RequestParam String confirmPassword) {

		try {
			passwordResetService.updatePassword(token, newPassword, confirmPassword);
			return ResponseEntity.ok(Map.of("message","Password updated Successfully!"));
		}catch (Exception e) {
			return ResponseEntity.ok(Map.of("message", e.getMessage()));
		}
	}

	@GetMapping("/getAgents")
	public ResponseEntity<?> getAllAgents(){

		Map<String, Object> responseMap = new HashMap<>();

		try {

			List<AgentDTO> agentsList = userRepository.findAgents();
			if(agentsList.isEmpty()) {
				responseMap.put("message", "No Agents found in the database.");
			}
			else {
				responseMap.put("agentsList", agentsList);
			}
		}
		catch(Exception e) {

			responseMap.put("message", e.getMessage());
		}
		return ResponseEntity.ok(responseMap);
	}

	@GetMapping("/getBuyers")
	public ResponseEntity<?> getAllBuyers(){
		Map<String, Object> responseMap = new HashMap<>();
		try {

			List<BuyerDTO> buyersList = userRepository.findBuyers();
			if(buyersList.isEmpty()) {
				responseMap.put("message", "No Buyers found in the database.");
			}
			else {
				responseMap.put("buyersList", buyersList);
			}
		}
		catch(Exception e) {

			responseMap.put("message", e.getMessage());
		}
		return ResponseEntity.ok(responseMap);
	}

	@GetMapping("/getSellers")
	public ResponseEntity<?> getAllSellers(){
		Map<String, Object> responseMap = new HashMap<>();
		try {

			List<SellerDTO> sellersList = userRepository.findSellers();
			if(sellersList.isEmpty()) {
				responseMap.put("message", "No Sellers found in the database.");
			}
			else {
				responseMap.put("sellersList", sellersList);
			}
		}
		catch(Exception e) {

			responseMap.put("message", e.getMessage());
		}
		return ResponseEntity.ok(responseMap);
	}
	
	   @GetMapping("/getUserProfile")
	    public ResponseEntity<?> getUserProfile(@RequestParam Long userId) {
		   Map<String, Object> responseMap = new HashMap<>();
		   List<UserDTO> user = userRepository.findUsers(userId);
	        if (user != null) {
	        	responseMap.put("userDetails", user);
	            return ResponseEntity.ok(responseMap);
	        } else {
	        	responseMap.put("message", "User not found");
	        	 return ResponseEntity.ok(responseMap);
	        }
	    }

	@PutMapping("/updateUserProfile")
	public ResponseEntity<?> updateUserProfile(@RequestParam Long userId, 
			@RequestParam String username, @RequestParam String gender, 
			@RequestParam String mobileNumber) {
		Map<String, Object> responseMap = new HashMap<>();
		Map<String, Object> updatedUserMap = new HashMap<>();
		try {
			User updatedUser = userService.updateUserProfile(userId, username,gender,mobileNumber);
			responseMap.put("message", "Profile updated successfully");
			updatedUserMap.put("username", updatedUser.getUsername());
			updatedUserMap.put("mobileNumber", updatedUser.getMobileNumber());
			updatedUserMap.put("gender", updatedUser.getGender());
			responseMap.put("updatedUser", updatedUserMap);
			return ResponseEntity.ok(responseMap);

		}catch(Exception e) {

			responseMap.put("message", e.getMessage());
			return ResponseEntity.ok(responseMap);
		}
		
	}

}
