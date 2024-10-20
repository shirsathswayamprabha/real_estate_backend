package com.realestatewebapp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.realestatewebapp.model.Property;
import com.realestatewebapp.model.PropertyDTO;
import com.realestatewebapp.model.PropertyImage;
import com.realestatewebapp.model.PropertyImageDTO;
import com.realestatewebapp.model.User;
import com.realestatewebapp.repository.PropertyImageRepository;
import com.realestatewebapp.repository.PropertyRepository;
import com.realestatewebapp.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PropertyService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private PropertyImageRepository propertyImageRepository;

	@Transactional
	public Property savePropertyWithImages(Property property, List<MultipartFile> files, Long userId) throws IOException {


		User user = userRepository.findById(userId)
				.orElseThrow(()-> new IllegalArgumentException("User not found with id: " + userId));

		property.setUser(user);
		Property savedProperty = propertyRepository.save(property);

		for(MultipartFile file : files) {
			PropertyImage propertyImage = new PropertyImage();
			propertyImage.setProperty(savedProperty);
			propertyImage.setFileName(file.getOriginalFilename());
			propertyImage.setFileType(file.getContentType());

			byte[] imageBytes = file.getBytes();
			System.out.println("propertyImage"+imageBytes);
			if(imageBytes !=null && imageBytes.length > 0) {
				propertyImage.setImageData(imageBytes);
				System.out.println("propertyImage"+propertyImage);
			}

			propertyImageRepository.save(propertyImage);
		}
		return savedProperty;
	}

	public List<PropertyDTO> getAllPropertyDetails(){


		List<Property> properties = propertyRepository.findAll();

	
		return properties.stream().map(property -> {

			List<PropertyImage> images = propertyImageRepository.findByPropertyId(property.getId());

			List<PropertyImageDTO> imageDTOs = images.stream()
					.map(PropertyImageDTO::new)
					.collect(Collectors.toList());

			return new PropertyDTO(property, imageDTOs);
		}).collect(Collectors.toList());
	}
	
	public List<PropertyDTO> getAllPropertyDetailsWithStatusRequest(){

		 List<Property> properties = propertyRepository.findByStatus("Request");

	
		return properties.stream().map(property -> {

			List<PropertyImage> images = propertyImageRepository.findByPropertyId(property.getId());

			List<PropertyImageDTO> imageDTOs = images.stream()
					.map(PropertyImageDTO::new)
					.collect(Collectors.toList());

			return new PropertyDTO(property, imageDTOs);
		}).collect(Collectors.toList());
	}
	
	public List<PropertyDTO> getAllPropertyDetailsWithStatusApproved(){

		 List<Property> properties = propertyRepository.findByStatus("Approved");

	
		return properties.stream().map(property -> {

			List<PropertyImage> images = propertyImageRepository.findByPropertyId(property.getId());

			List<PropertyImageDTO> imageDTOs = images.stream()
					.map(PropertyImageDTO::new)
					.collect(Collectors.toList());

			return new PropertyDTO(property, imageDTOs);
		}).collect(Collectors.toList());
	}
	
	public List<PropertyDTO> getAllPropertyDetailsWithStatusBuy(){

		 List<Property> properties = propertyRepository.findByStatus("Buy");

	
		return properties.stream().map(property -> {

			List<PropertyImage> images = propertyImageRepository.findByPropertyId(property.getId());

			List<PropertyImageDTO> imageDTOs = images.stream()
					.map(PropertyImageDTO::new)
					.collect(Collectors.toList());

			return new PropertyDTO(property, imageDTOs);
		}).collect(Collectors.toList());
	}
	
	public List<PropertyDTO> getAllPropertyDetailsWithStatusBookmarked(Long userId){

		List<Property> properties = propertyRepository.findByStatusAndUserId("Bookmarked", userId);

	
		return properties.stream().map(property -> {

			List<PropertyImage> images = propertyImageRepository.findByPropertyId(property.getId());

			List<PropertyImageDTO> imageDTOs = images.stream()
					.map(PropertyImageDTO::new)
					.collect(Collectors.toList());

			return new PropertyDTO(property, imageDTOs);
		}).collect(Collectors.toList());
	}
	
	@Transactional
	public Property sendRequest(Long propertyId, String status) {

		Property property = propertyRepository.findById(propertyId)
				.orElseThrow(()-> new IllegalArgumentException("Property not found with id: " + propertyId));

		property.setStatus(status);

		return propertyRepository.save(property);


	}

	@Transactional
	public Map<String,Object> setBookmarkStatus(Long userId,String status, Long propertyId) {

		Map<String, Object> responseMap = new HashMap<>();
		if (!userRepository.existsById(userId)) {
			responseMap.put("message", "User not found");
			return responseMap;
		}
		if (!propertyRepository.existsById(propertyId)) {
			responseMap.put("message", "Property not found");
			return responseMap;
		}

		// Find the existing bookmark or create a new one
		Optional<Property> existingBookmark = propertyRepository.findByUserIdAndPropertyId(userId, propertyId);

		if (existingBookmark.isPresent()) {
			// If it exists, update the status
			propertyRepository.updateStatus(userId, propertyId, status);
			responseMap.put("message", "Bookmark status updated successfully");
			return responseMap;
		} else {
			// If it doesn't exist, create a new bookmark entry
			Property newBookmark = new Property();
			newBookmark.setUser(userRepository.findById(userId).get());
			newBookmark.setId(propertyId);
			newBookmark.setStatus(status);
			propertyRepository.save(newBookmark);
			responseMap.put("message", "Bookmark added successfully");
			return responseMap;
		}



	}
	public List<Property> getApprovedPropertylist(Long userId) {

		Optional<User> user = userRepository.findById(userId);

		if(!user.isPresent()) {
			throw new IllegalArgumentException("User not found with id: " + userId);
		}

		return propertyRepository.findByUser(user.get());
	}



//	public List<PropertyDTO> getBookmarkedProperties(Long userId) {
//		return PropertyService.get(userId);
//	}
	public Property buyProperty(Long userId, String status) {
		// Find the property associated with the userId (assuming userId is tied to the property)
		Property property = propertyRepository.findByUserId(userId);

		// Check if property is found and status is not already 'buy'
		if (property != null && !status.equalsIgnoreCase("Buy")) {
			// Update the status of the property to 'buy'
			property.setStatus("Buy");

			// Save the updated property in the database
			return propertyRepository.save(property);
		}

		return null; // Return null if property is not found or invalid status
	}
	
	   // Method to get property details by ID
    public Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId).orElse(null);
    }
    public User getUserDetailsById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
    // Method to get agent's email by agent ID
    public String getAgentEmail(Long userId) {
        // Assuming User is the entity for agents and has email field
        return userRepository.findById(userId).map(user -> user.getEmail()).orElse(null);
    }
}
