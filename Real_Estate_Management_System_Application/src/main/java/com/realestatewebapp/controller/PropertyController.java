package com.realestatewebapp.controller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestatewebapp.model.Property;
import com.realestatewebapp.model.PropertyDTO;
import com.realestatewebapp.model.SellerDTO;
import com.realestatewebapp.model.User;
import com.realestatewebapp.repository.PropertyRepository;
import com.realestatewebapp.repository.UserRepository;
import com.realestatewebapp.service.EmailService;
import com.realestatewebapp.service.PropertyService;
import com.realestatewebapp.service.UserService;

import ch.qos.logback.core.util.StringUtil;
import jakarta.mail.MessagingException;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/properties")
public class PropertyController {

	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;
	@GetMapping("/getAllProperties")
	public ResponseEntity<?> getAllProperties(){

		Map<String, Object> responseMap = new HashMap<>();

		try {
			List<PropertyDTO> propertyData = propertyService.getAllPropertyDetails();
			System.out.println("Property Data>>"+propertyService.getAllPropertyDetails());
			if(propertyData.isEmpty()) {
				responseMap.put("message", "No properties found in the database.");
			}
			else {
				responseMap.put("propertyData", propertyData);
			}
		}
		catch(Exception e) {

			responseMap.put("message", e.getMessage());
		}
		return ResponseEntity.ok(responseMap);
	}


	@PostMapping("/addProperty")
	public ResponseEntity<?> uploadPropertyWithImages(@RequestParam("images") List<MultipartFile> files,
			@RequestParam("property") String propertyJson, 
			@RequestParam("userId")Long userId) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		Property property = objectMapper.readValue(propertyJson, Property.class);
		Map<String, Object> responseMap = new HashMap<>();

		if (files.isEmpty()) {
			return ResponseEntity.badRequest().body(null); // or return an error message
		}
		// Save the property with the images
		Property savedProperty = propertyService.savePropertyWithImages(property, files, userId);
		System.out.println("savedProperty"+savedProperty);
		responseMap.put("message", "Property registered successfully");
		return ResponseEntity.ok(responseMap);
	}

	//	Use for admin and agent both 
	//	after agent send request status update as "Request" 
	//	admin send request or click on approve status change to "Approved"
	@PostMapping("/sendRequest")
	public ResponseEntity<?> sendRequest(@RequestParam("propertyId") Long propertyId, 
			@RequestParam("status") String status){

		propertyService.sendRequest(propertyId, status);
		Map<String, Object> responseMap = new HashMap<>();
		if(status.equals("Request")) {
			responseMap.put("message", "Request Send Successfully!");
		}
		else if(status.equals("Approved")) {
			responseMap.put("message", "Property Approved Successfully!");
		}
		else if(status.equals("Buy")) {
			responseMap.put("message", "Property Buy Request send to Agent Successfully!");
		}
		
		return ResponseEntity.ok(responseMap);

	}

	@PostMapping("/getBookmarkedProperties")
	public ResponseEntity<?> getBookmarkedProperties(@RequestParam("userId") Long userId){
		Map<String, Object> responseMap = new HashMap<>();
		List<PropertyDTO> bookmarkedProperties = propertyService.getAllPropertyDetailsWithStatusBookmarked(userId);
		responseMap.put("bookmarkedProperties", bookmarkedProperties);
		// Check if properties are found
		if (bookmarkedProperties.isEmpty()) {
			responseMap.put("message", "No Data found");
			return ResponseEntity.ok(responseMap); // No properties found
		}

		// Return the list of bookmarked properties
		return ResponseEntity.ok(responseMap);

	}
	@PostMapping("/getApprovedPropertylist")
	public ResponseEntity<?> getApprovedPropertylist(@RequestParam("userId")Long userId){

		Map<String, Object> responseMap = new HashMap<>();

		try {
			// Fetch properties for the given userId
			List<PropertyDTO> approvedProperties = propertyService.getAllPropertyDetailsWithStatusApproved();
			//					propertyService.getApprovedPropertylist(userId);

			if (approvedProperties.isEmpty()) {
				responseMap.put("message", "No properties found for user with id: " + userId);
			} else {
				responseMap.put("approvedProperties", approvedProperties);
			}

		} catch (Exception e) {
			responseMap.put("message", "Error fetching properties for user with id: " + userId);
		}
		return ResponseEntity.ok(responseMap);

	}

	//	list of properties where status = "Request"

	@GetMapping("/getApproveRequestPropertyDetails")
	public ResponseEntity<?> getApproveRequestPropertyDetails(){
		Map<String, Object> responseMap = new HashMap<>();
		try {

			List<PropertyDTO> propertyList = propertyService.getAllPropertyDetailsWithStatusRequest();

			if(propertyList.isEmpty()) {
				responseMap.put("message", "No Property details found in the database.");
			}
			else {
				responseMap.put("propertyList", propertyList);
			}
		}
		catch(Exception e) {

			responseMap.put("message", e.getMessage());
		}
		return ResponseEntity.ok(responseMap);
	}

	@PutMapping("/bookmarked")
	public ResponseEntity<Map<String,Object>> setBookmarkedStatus(@RequestParam Long userId, @RequestParam Long propertyId, @RequestParam String status) {
		Map<String, Object> responseMap = new HashMap<>();
		if (!userRepository.existsById(userId)) {
			responseMap.put("message", "User not found");
			return ResponseEntity.ok(responseMap);
		}
		if (!propertyRepository.existsById(propertyId)) {
			responseMap.put("message", "Property not found");
			return ResponseEntity.ok(responseMap);
		}

		// Find the existing bookmark or create a new one
		Optional<Property> existingBookmark = propertyRepository.findByUserIdAndPropertyId(userId, propertyId);

		if (existingBookmark.isPresent()) {
			// If it exists, update the status
			propertyRepository.updateStatus(userId, propertyId, status);
			responseMap.put("message", "Bookmark status updated successfully");
			return ResponseEntity.ok(responseMap);
		} else {
			// If it doesn't exist, create a new bookmark entry
			Property newBookmark = new Property();
			newBookmark.setUser(userRepository.findById(userId).get());
			newBookmark.setId(propertyId);
			newBookmark.setStatus(status);
			propertyRepository.save(newBookmark);
			responseMap.put("message", "Bookmark added successfully");
			return ResponseEntity.ok(responseMap);
		}
	}

	@PostMapping("/buyProperty")
	public ResponseEntity<?> buyProperty(@RequestParam Long userId, @RequestParam String status){
		Map<String, Object> responseMap = new HashMap<>();
		try {

			// Call service method to update property status
			Property updatedProperty = propertyService.buyProperty(userId, status);

			if (updatedProperty != null) {
				responseMap.put("updatedProperty", updatedProperty);
				return ResponseEntity.ok(responseMap);  // Property status updated successfully
			} else {
				responseMap.put("message", "Property not found or invalid status.");
				return ResponseEntity.ok(responseMap); 
			}
		} catch (Exception e) {
			responseMap.put("message", "Property not found or invalid status.");
			return ResponseEntity.ok("Error occurred while updating property status: " + e.getMessage()); 
		}
	}

	@PostMapping("/hireAgent")
	public ResponseEntity<?> hireAgent(@RequestParam Long userId) {
		Map<String, Object> responseMap = new HashMap<>();
		try {

			// Get property details using propertyId
//			Property property = propertyService.getPropertyById(propertyId);
//
//			if (property == null) {
//				responseMap.put("message", "Property not found ");
//				return ResponseEntity.ok(responseMap); 
//			}

			// Get agent email (assume agent's email is fetched from a user or agent service)
			User user = propertyService.getUserDetailsById(userId);
			String agentEmail = user.getEmail();

			if (agentEmail == null) {
				responseMap.put("message", "Agent not found ");
				return ResponseEntity.ok(responseMap); 
			}


			String sellerDetails = "Seller Details:<br/>" +
					"Seller: " + user.getUsername()+"<br/>" +
					"Seller Mobile Number: " + user.getMobileNumber()+"<br/>" +
					"Seller Email ID: " + user.getEmail()+"<br/>" +
					"Seller Gender: " + user.getGender();

			emailService.sendHireAgentEmail(agentEmail, sellerDetails);


			responseMap.put("message", "Email sent to agent.");
			return ResponseEntity.ok(responseMap);

		} catch (MessagingException e) {
			responseMap.put("message", "Error occurred while sending email: " + e.getMessage());
			return ResponseEntity.ok(responseMap);
		}
	}

	@PostMapping("/sendBuyPropertyDetails")
	public ResponseEntity<?> sendBuyPropertyDetails(@RequestParam Long userId) {
		Map<String, Object> responseMap = new HashMap<>();
		try {

			// Get property details using propertyId
			User user = propertyService.getUserDetailsById(userId);
			String agentEmail = user.getEmail();


			if (agentEmail == null) {
				responseMap.put("message", "Agent not found ");
				return ResponseEntity.ok(responseMap); 
			}		

			String buyerDetails = " Buyer Details:<br/>" +
					"Buyer: " + user.getUsername()+"<br/>" +
					"Buyer Mobile Number: " + user.getMobileNumber()+"<br/>" +
					"Buyer Email ID: " + user.getEmail()+"<br/>" +
					"Buyer Gender: " + user.getGender();
			
					emailService.sendBuyPropertyAgentEmail(agentEmail, buyerDetails);

					responseMap.put("message", "Email sent to agent.");
					return ResponseEntity.ok(responseMap);

		} catch (MessagingException e) {
			responseMap.put("message", "Error occurred while sending email: " + e.getMessage());
			return ResponseEntity.ok(responseMap);
		}
	}
	
	@PostMapping("/getBuiedPropertylist")
	public ResponseEntity<?> getBuiedPropertylist(@RequestParam("userId")Long userId){

		Map<String, Object> responseMap = new HashMap<>();

		try {
			// Fetch properties for the given userId
			List<PropertyDTO> buiedProperties = propertyService.getAllPropertyDetailsWithStatusBuy();
			

			if (buiedProperties.isEmpty()) {
				responseMap.put("message", "No properties found for user with id: " + userId);
			} else {
				responseMap.put("buiedProperties", buiedProperties);
			}

		} catch (Exception e) {
			responseMap.put("message", "Error fetching properties for user with id: " + userId);
		}
		return ResponseEntity.ok(responseMap);

	}

}
